package cn.iocoder.yudao.module.childhealth.dal.mysql.screeningconfig;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screeningconfig.HealthArticleDO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 健康文章 Mapper
 *
 * 模块: C.五健筛查配置
 * 创建日期: 2026-07-20
 */
@Mapper
public interface HealthArticleMapper extends BaseMapperX<HealthArticleDO> {

    /**
     * 按文章编码查询
     */
    default HealthArticleDO selectByArticleCode(String articleCode) {
        return selectOne(HealthArticleDO::getArticleCode, articleCode);
    }

    /**
     * 按分类查询已发布文章（置顶优先）
     */
    default List<HealthArticleDO> selectListByCategory(String category) {
        return selectList(new LambdaQueryWrapperX<HealthArticleDO>()
                .eqIfPresent(HealthArticleDO::getCategory, category)
                .eqIfPresent(HealthArticleDO::getPublishStatus, 2)
                .orderByDesc(HealthArticleDO::getIsTop)
                .orderByDesc(HealthArticleDO::getPublishTime));
    }

    /**
     * 查询所有已发布文章（置顶优先）
     */
    default List<HealthArticleDO> selectPublishedList() {
        return selectList(new LambdaQueryWrapperX<HealthArticleDO>()
                .eqIfPresent(HealthArticleDO::getPublishStatus, 2)
                .orderByDesc(HealthArticleDO::getIsTop)
                .orderByDesc(HealthArticleDO::getPublishTime));
    }

    /**
     * 按发布状态查询
     */
    default List<HealthArticleDO> selectListByPublishStatus(Integer publishStatus) {
        return selectList(new LambdaQueryWrapperX<HealthArticleDO>()
                .eqIfPresent(HealthArticleDO::getPublishStatus, publishStatus)
                .orderByDesc(HealthArticleDO::getPublishTime));
    }

    /**
     * 按标题模糊查询已发布文章
     */
    default List<HealthArticleDO> selectListByTitleLike(String title) {
        return selectList(new LambdaQueryWrapperX<HealthArticleDO>()
                .likeIfPresent(HealthArticleDO::getTitle, title)
                .eqIfPresent(HealthArticleDO::getPublishStatus, 2)
                .orderByDesc(HealthArticleDO::getPublishTime));
    }

    /**
     * 查询需要定时发布且到时间的文章
     */
    default List<HealthArticleDO> selectListToPublish(LocalDateTime currentTime) {
        return selectList(new LambdaQueryWrapperX<HealthArticleDO>()
                .eqIfPresent(HealthArticleDO::getPublishStatus, 1)
                .leIfPresent(HealthArticleDO::getPublishTime, currentTime));
    }

}
