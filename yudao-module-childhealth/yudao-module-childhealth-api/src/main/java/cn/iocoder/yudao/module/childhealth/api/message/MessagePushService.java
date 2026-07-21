package cn.iocoder.yudao.module.childhealth.api.message;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.api.message.dto.MessagePushDTO.*;

import java.util.List;

/**
 * 消息推送Service接口
 */
public interface MessagePushService {

    // ==================== 消息推送配置管理 ====================

    /**
     * 创建消息推送配置
     */
    Long createMessagePushConfig(MessagePushConfigCreateRequest request);

    /**
     * 更新消息推送配置状态
     */
    void updateMessagePushConfigStatus(Long id, Integer status);

    /**
     * 获取消息推送配置详情
     */
    MessagePushConfigResponse getMessagePushConfig(Long id);

    /**
     * 获取所有启用的消息推送配置
     */
    List<MessagePushConfigResponse> getActiveMessagePushConfigs();

    /**
     * 根据推送类型获取配置
     */
    List<MessagePushConfigResponse> getMessagePushConfigsByType(Integer pushType);

    // ==================== 消息推送管理 ====================

    /**
     * 发送消息
     */
    Long sendMessage(MessagePushRequest request);

    /**
     * 批量发送消息
     */
    void batchSendMessage(BatchMessagePushRequest request);

    /**
     * 取消消息推送
     */
    void cancelMessage(Long id);

    /**
     * 重试发送失败的消息
     */
    void retryMessage(Long id);

    /**
     * 标记消息已读
     */
    void markMessageAsRead(Long id);

    /**
     * 分页查询消息推送记录
     */
    PageResult<MessagePushLogResponse> getMessagePushLogPage(MessagePushQueryRequest request);

    /**
     * 获取消息推送详情
     */
    MessagePushLogResponse getMessagePushLog(Long id);

    /**
     * 获取目标的消息推送历史
     */
    List<MessagePushLogResponse> getMessagePushHistory(Integer targetType, Long targetId);

    /**
     * 获取消息推送统计
     */
    MessagePushStatisticsResponse getMessagePushStatistics();

    // ==================== 健康文章管理 ====================

    /**
     * 创建健康文章
     */
    Long createHealthArticle(HealthArticleCreateRequest request);

    /**
     * 更新健康文章
     */
    void updateHealthArticle(HealthArticleUpdateRequest request);

    /**
     * 删除健康文章
     */
    void deleteHealthArticle(Long id);

    /**
     * 发布健康文章
     */
    void publishHealthArticle(Long id);

    /**
     * 下架健康文章
     */
    void unpublishHealthArticle(Long id);

    /**
     * 获取健康文章详情
     */
    HealthArticleResponse getHealthArticle(Long id);

    /**
     * 分页查询健康文章
     */
    PageResult<HealthArticleResponse> getHealthArticlePage(String category, Integer status, Integer pageNum, Integer pageSize);

    /**
     * 获取已发布的健康文章列表
     */
    List<HealthArticleResponse> getPublishedArticles();

    /**
     * 根据分类获取文章
     */
    List<HealthArticleResponse> getArticlesByCategory(String category);

    /**
     * 获取置顶文章
     */
    List<HealthArticleResponse> getTopArticles();

    /**
     * 增加文章阅读量
     */
    void increaseReadCount(Long articleId);

    /**
     * 记录文章阅读日志
     */
    void logArticleRead(Long articleId, Integer readerType, Long readerId, Integer readDuration);

    /**
     * 获取文章阅读日志
     */
    List<ArticleReadLogResponse> getArticleReadLogs(Long articleId);
}