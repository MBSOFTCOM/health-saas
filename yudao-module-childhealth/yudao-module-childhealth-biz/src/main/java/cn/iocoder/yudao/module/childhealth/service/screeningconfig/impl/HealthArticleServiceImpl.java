package cn.iocoder.yudao.module.childhealth.service.screeningconfig.impl;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screeningconfig.HealthArticleDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.screeningconfig.HealthArticleMapper;
import cn.iocoder.yudao.module.childhealth.service.screeningconfig.HealthArticleService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.HEALTH_ARTICLE_CODE_DUPLICATE;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.HEALTH_ARTICLE_NOT_EXISTS;

/**
 * 健康文章 Service 实现类
 *
 * 模块: C.五健筛查配置
 * 创建日期: 2026-07-20
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class HealthArticleServiceImpl implements HealthArticleService {

    @Resource
    private HealthArticleMapper healthArticleMapper;

    @Override
    public Long createHealthArticle(Object saveReqVO) {
        // TODO 后续替换为 HealthArticleSaveReqVO
        HealthArticleDO article = BeanUtils.toBean(saveReqVO, HealthArticleDO.class);
        // 编码唯一性校验
        if (article.getArticleCode() != null
                && healthArticleMapper.selectByArticleCode(article.getArticleCode()) != null) {
            throw exception(HEALTH_ARTICLE_CODE_DUPLICATE);
        }
        healthArticleMapper.insert(article);
        return article.getId();
    }

    @Override
    public void updateHealthArticle(Object saveReqVO) {
        // TODO 后续替换为 HealthArticleSaveReqVO
        HealthArticleDO updateObj = BeanUtils.toBean(saveReqVO, HealthArticleDO.class);
        validateHealthArticleExists(updateObj.getId());
        healthArticleMapper.updateById(updateObj);
    }

    @Override
    public void deleteHealthArticle(Long id) {
        validateHealthArticleExists(id);
        healthArticleMapper.deleteById(id);
    }

    @Override
    public HealthArticleDO getHealthArticle(Long id) {
        return healthArticleMapper.selectById(id);
    }

    @Override
    public PageResult<HealthArticleDO> getHealthArticlePage(PageParam pageParam) {
        // TODO 后续替换为 HealthArticlePageReqVO，并增加查询条件
        return healthArticleMapper.selectPage(pageParam, null);
    }

    @Override
    public int publishArticle() {
        // TODO 实现定时发布：扫描 publishStatus=1 且 publishTime <= 当前时间的文章，将其置为 publishStatus=2
        log.info("[publishArticle] 执行定时发布任务 currentTime={}", LocalDateTime.now());
        List<HealthArticleDO> toPublishList = healthArticleMapper.selectListToPublish(LocalDateTime.now());
        int count = 0;
        for (HealthArticleDO article : toPublishList) {
            article.setPublishStatus(2);
            healthArticleMapper.updateById(article);
            count++;
        }
        return count;
    }

    @Override
    public void topArticle(Long id, Integer isTop) {
        validateHealthArticleExists(id);
        // TODO 可增加业务校验：仅已发布文章可置顶
        HealthArticleDO updateObj = new HealthArticleDO();
        updateObj.setId(id);
        updateObj.setIsTop(isTop);
        healthArticleMapper.updateById(updateObj);
    }

    @Override
    public List<HealthArticleDO> selectPublishedList() {
        return healthArticleMapper.selectPublishedList();
    }

    private void validateHealthArticleExists(Long id) {
        if (id == null || healthArticleMapper.selectById(id) == null) {
            throw exception(HEALTH_ARTICLE_NOT_EXISTS);
        }
    }

}
