package cn.iocoder.yudao.module.childhealth.service.message;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.api.message.MessagePushService;
import cn.iocoder.yudao.module.childhealth.api.message.dto.MessagePushDTO.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screeningconfig.HealthArticleDO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.ops.MessagePushLogDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.screeningconfig.HealthArticleMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.ops.MessagePushLogMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.ops.MessagePushConfigMapper;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.ops.MessagePushConfigDO;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 消息推送Service实现
 */
@Slf4j
@Service
public class MessagePushServiceImpl implements MessagePushService {

    @Resource
    private MessagePushConfigMapper messagePushConfigMapper;
    @Resource
    private MessagePushLogMapper messagePushLogMapper;
    @Resource
    private HealthArticleMapper healthArticleMapper;

    // ==================== 消息推送配置管理 ====================

    @Override
    @Transactional
    public Long createMessagePushConfig(MessagePushConfigCreateRequest request) {
        if (messagePushConfigMapper.selectCount(Wrappers.<MessagePushConfigDO>lambdaQuery()
                .eq(MessagePushConfigDO::getConfigCode, request.getConfigCode())) > 0) {
            throw new ServiceException(400, "配置编码已存在");
        }

        MessagePushConfigDO config = new MessagePushConfigDO();
        BeanUtils.copyProperties(request, config);
        config.setStatus(1);
        config.setCreateTime(LocalDateTime.now());
        messagePushConfigMapper.insert(config);
        return config.getId();
    }

    @Override
    @Transactional
    public void updateMessagePushConfigStatus(Long id, Integer status) {
        MessagePushConfigDO config = messagePushConfigMapper.selectById(id);
        if (config == null) {
            throw new ServiceException(404, "配置不存在");
        }
        config.setStatus(status);
        messagePushConfigMapper.updateById(config);
    }

    @Override
    public MessagePushConfigResponse getMessagePushConfig(Long id) {
        MessagePushConfigDO config = messagePushConfigMapper.selectById(id);
        return config == null ? null : convertToMessagePushConfigResponse(config);
    }

    @Override
    public List<MessagePushConfigResponse> getActiveMessagePushConfigs() {
        return messagePushConfigMapper.selectList(Wrappers.<MessagePushConfigDO>lambdaQuery()
                        .eq(MessagePushConfigDO::getStatus, 1))
                .stream()
                .map(this::convertToMessagePushConfigResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MessagePushConfigResponse> getMessagePushConfigsByType(Integer pushType) {
        return messagePushConfigMapper.selectList(Wrappers.<MessagePushConfigDO>lambdaQuery()
                        .eq(MessagePushConfigDO::getPushType, pushType)
                        .eq(MessagePushConfigDO::getStatus, 1))
                .stream()
                .map(this::convertToMessagePushConfigResponse)
                .collect(Collectors.toList());
    }

    // ==================== 消息推送管理 ====================

    @Override
    @Transactional
    public Long sendMessage(MessagePushRequest request) {
        MessagePushLogDO log = new MessagePushLogDO();
        BeanUtils.copyProperties(request, log);

        // 立即发送或计划发送
        if (request.getScheduledPushTime() == null) {
            log.setPushTime(LocalDateTime.now());
            log.setPushStatus(doSendMessage(request) ? 1 : 2); // 1成功 2失败
        } else {
            log.setPushStatus(3); // 待推送
        }

        log.setIsRead(false);
        log.setCreateTime(LocalDateTime.now());
        messagePushLogMapper.insert(log);
        return log.getId();
    }

    @Override
    @Transactional
    public void batchSendMessage(BatchMessagePushRequest request) {
        for (Long targetId : request.getTargetIds()) {
            MessagePushRequest singleRequest = new MessagePushRequest();
            BeanUtils.copyProperties(request, singleRequest);
            singleRequest.setTargetId(targetId);
            sendMessage(singleRequest);
        }
    }

    @Override
    @Transactional
    public void cancelMessage(Long id) {
        MessagePushLogDO log = messagePushLogMapper.selectById(id);
        if (log == null) {
            throw new ServiceException(404, "消息不存在");
        }
        if (log.getPushStatus() != 3) {
            throw new ServiceException(400, "只能取消待推送的消息");
        }
        log.setPushStatus(4); // 已取消
        messagePushLogMapper.updateById(log);
    }

    @Override
    @Transactional
    public void retryMessage(Long id) {
        MessagePushLogDO log = messagePushLogMapper.selectById(id);
        if (log == null) {
            throw new ServiceException(404, "消息不存在");
        }
        if (log.getPushStatus() != 2) {
            throw new ServiceException(400, "只能重试发送失败的消息");
        }

        // 重新发送
        boolean success = doSendMessage(buildPushRequest(log));
        log.setPushStatus(success ? 1 : 2);
        log.setPushTime(LocalDateTime.now());
        messagePushLogMapper.updateById(log);
    }

    @Override
    @Transactional
    public void markMessageAsRead(Long id) {
        MessagePushLogDO log = messagePushLogMapper.selectById(id);
        if (log != null) {
            log.setIsRead(true);
            messagePushLogMapper.updateById(log);
        }
    }

    @Override
    public PageResult<MessagePushLogResponse> getMessagePushLogPage(MessagePushQueryRequest request) {
        List<MessagePushLogDO> list = messagePushLogMapper.selectList(Wrappers.<MessagePushLogDO>lambdaQuery()
                .eq(request.getConfigId() != null, MessagePushLogDO::getConfigId, request.getConfigId())
                .eq(request.getPushType() != null, MessagePushLogDO::getPushType, request.getPushType())
                .eq(request.getPushChannel() != null, MessagePushLogDO::getPushChannel, request.getPushChannel())
                .eq(request.getTargetType() != null, MessagePushLogDO::getTargetType, request.getTargetType())
                .eq(request.getTargetId() != null, MessagePushLogDO::getTargetId, request.getTargetId())
                .eq(request.getPushStatus() != null, MessagePushLogDO::getPushStatus, request.getPushStatus())
                .orderByDesc(MessagePushLogDO::getCreateTime));

        List<MessagePushLogResponse> responseList = list.stream()
                .map(this::convertToMessagePushLogResponse)
                .collect(Collectors.toList());

        return new PageResult<>(responseList, (long) responseList.size());
    }

    @Override
    public MessagePushLogResponse getMessagePushLog(Long id) {
        MessagePushLogDO log = messagePushLogMapper.selectById(id);
        return log == null ? null : convertToMessagePushLogResponse(log);
    }

    @Override
    public List<MessagePushLogResponse> getMessagePushHistory(Integer targetType, Long targetId) {
        return messagePushLogMapper.selectList(Wrappers.<MessagePushLogDO>lambdaQuery()
                        .eq(MessagePushLogDO::getTargetType, targetType)
                        .eq(MessagePushLogDO::getTargetId, targetId)
                        .orderByDesc(MessagePushLogDO::getCreateTime))
                .stream()
                .map(this::convertToMessagePushLogResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MessagePushStatisticsResponse getMessagePushStatistics() {
        List<MessagePushLogDO> allLogs = messagePushLogMapper.selectList(null);

        MessagePushStatisticsResponse stats = new MessagePushStatisticsResponse();
        stats.setTotalPushed(allLogs.size());
        stats.setSuccessCount((int) allLogs.stream().filter(l -> l.getPushStatus() == 1).count());
        stats.setFailedCount((int) allLogs.stream().filter(l -> l.getPushStatus() == 2).count());
        stats.setPendingCount((int) allLogs.stream().filter(l -> l.getPushStatus() == 3).count());
        stats.setCancelledCount((int) allLogs.stream().filter(l -> l.getPushStatus() == 4).count());
        stats.setReadCount((int) allLogs.stream().filter(l -> Boolean.TRUE.equals(l.getIsRead())).count());
        stats.setUnreadCount((int) allLogs.stream().filter(l -> !Boolean.TRUE.equals(l.getIsRead())).count());

        return stats;
    }

    // ==================== 健康文章管理 ====================

    @Override
    @Transactional
    public Long createHealthArticle(HealthArticleCreateRequest request) {
        if (healthArticleMapper.selectCount(Wrappers.<HealthArticleDO>lambdaQuery()
                .eq(HealthArticleDO::getArticleCode, request.getArticleCode())) > 0) {
            throw new ServiceException(400, "文章编码已存在");
        }

        HealthArticleDO article = new HealthArticleDO();
        BeanUtils.copyProperties(request, article);
        article.setViewCount(0);
        article.setPublishStatus(0); // 默认草稿（保留版本：0草稿/1待发布/2已发布/3已下线）
        healthArticleMapper.insert(article);
        return article.getId();
    }

    @Override
    @Transactional
    public void updateHealthArticle(HealthArticleUpdateRequest request) {
        HealthArticleDO existing = healthArticleMapper.selectById(request.getId());
        if (existing == null) {
            throw new ServiceException(404, "文章不存在");
        }

        HealthArticleDO article = new HealthArticleDO();
        BeanUtils.copyProperties(request, article);
        healthArticleMapper.updateById(article);
    }

    @Override
    @Transactional
    public void deleteHealthArticle(Long id) {
        healthArticleMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void publishHealthArticle(Long id) {
        HealthArticleDO article = healthArticleMapper.selectById(id);
        if (article == null) {
            throw new ServiceException(404, "文章不存在");
        }
        article.setPublishStatus(2); // 已发布
        article.setPublishTime(LocalDateTime.now());
        healthArticleMapper.updateById(article);
    }

    @Override
    @Transactional
    public void unpublishHealthArticle(Long id) {
        HealthArticleDO article = healthArticleMapper.selectById(id);
        if (article == null) {
            throw new ServiceException(404, "文章不存在");
        }
        article.setPublishStatus(3); // 已下架
        healthArticleMapper.updateById(article);
    }

    @Override
    public HealthArticleResponse getHealthArticle(Long id) {
        HealthArticleDO article = healthArticleMapper.selectById(id);
        return article == null ? null : convertToHealthArticleResponse(article);
    }

    @Override
    public PageResult<HealthArticleResponse> getHealthArticlePage(String category, Integer status,
                                                                     Integer pageNum, Integer pageSize) {
        List<HealthArticleDO> list = healthArticleMapper.selectList(Wrappers.<HealthArticleDO>lambdaQuery()
                .eq(category != null, HealthArticleDO::getCategory, category)
                .eq(status != null, HealthArticleDO::getPublishStatus, status)
                .orderByDesc(HealthArticleDO::getIsTop)
                .orderByDesc(HealthArticleDO::getPublishTime));

        List<HealthArticleResponse> responseList = list.stream()
                .map(this::convertToHealthArticleResponse)
                .collect(Collectors.toList());

        return new PageResult<>(responseList, (long) responseList.size());
    }

    @Override
    public List<HealthArticleResponse> getPublishedArticles() {
        return healthArticleMapper.selectList(Wrappers.<HealthArticleDO>lambdaQuery()
                        .eq(HealthArticleDO::getPublishStatus, 2)
                        .orderByDesc(HealthArticleDO::getIsTop)
                        .orderByDesc(HealthArticleDO::getPublishTime))
                .stream()
                .map(this::convertToHealthArticleResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<HealthArticleResponse> getArticlesByCategory(String category) {
        return healthArticleMapper.selectList(Wrappers.<HealthArticleDO>lambdaQuery()
                        .eq(HealthArticleDO::getCategory, category)
                        .eq(HealthArticleDO::getPublishStatus, 2)
                        .orderByDesc(HealthArticleDO::getPublishTime))
                .stream()
                .map(this::convertToHealthArticleResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<HealthArticleResponse> getTopArticles() {
        return healthArticleMapper.selectList(Wrappers.<HealthArticleDO>lambdaQuery()
                        .eq(HealthArticleDO::getIsTop, 1)
                        .eq(HealthArticleDO::getPublishStatus, 2)
                        .orderByDesc(HealthArticleDO::getPublishTime))
                .stream()
                .map(this::convertToHealthArticleResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void increaseReadCount(Long articleId) {
        HealthArticleDO article = healthArticleMapper.selectById(articleId);
        if (article != null) {
            article.setViewCount((article.getViewCount() == null ? 0 : article.getViewCount()) + 1);
            healthArticleMapper.updateById(article);
        }
    }

    @Override
    @Transactional
    public void logArticleRead(Long articleId, Integer readerType, Long readerId, Integer readDuration) {
        // 简化实现,实际需要ArticleReadLogMapper
        log.info("记录文章阅读日志: articleId={}, readerType={}, readerId={}, readDuration={}",
                articleId, readerType, readerId, readDuration);
    }

    @Override
    public List<ArticleReadLogResponse> getArticleReadLogs(Long articleId) {
        // 简化实现
        return List.of();
    }

    // ==================== 辅助方法 ====================

    private boolean doSendMessage(MessagePushRequest request) {
        // 实际发送消息的逻辑
        // 这里简化实现,实际需要对接短信、微信、APP推送等渠道
        try {
            log.info("发送消息: pushChannel={}, targetId={}, content={}",
                    request.getPushChannel(), request.getTargetId(), request.getPushContent());
            return true; // 模拟发送成功
        } catch (Exception e) {
            log.error("发送消息失败", e);
            return false;
        }
    }

    private MessagePushRequest buildPushRequest(MessagePushLogDO log) {
        MessagePushRequest request = new MessagePushRequest();
        BeanUtils.copyProperties(log, request);
        return request;
    }

    private MessagePushConfigResponse convertToMessagePushConfigResponse(MessagePushConfigDO config) {
        MessagePushConfigResponse response = new MessagePushConfigResponse();
        BeanUtils.copyProperties(config, response);
        return response;
    }

    private MessagePushLogResponse convertToMessagePushLogResponse(MessagePushLogDO log) {
        MessagePushLogResponse response = new MessagePushLogResponse();
        BeanUtils.copyProperties(log, response);
        return response;
    }

    private HealthArticleResponse convertToHealthArticleResponse(HealthArticleDO article) {
        HealthArticleResponse response = new HealthArticleResponse();
        BeanUtils.copyProperties(article, response);
        return response;
    }
}