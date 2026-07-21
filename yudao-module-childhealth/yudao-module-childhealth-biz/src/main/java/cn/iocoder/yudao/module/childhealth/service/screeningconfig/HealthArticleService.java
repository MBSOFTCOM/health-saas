package cn.iocoder.yudao.module.childhealth.service.screeningconfig;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screeningconfig.HealthArticleDO;

import java.util.List;

/**
 * 健康文章 Service 接口
 *
 * 模块: C.五健筛查配置
 * 创建日期: 2026-07-20
 *
 * @author 芋道源码
 */
public interface HealthArticleService {

    /**
     * 创建健康文章
     *
     * @param saveReqVO 创建信息（后续替换为 HealthArticleSaveReqVO）
     * @return 编号
     */
    Long createHealthArticle(Object saveReqVO);

    /**
     * 更新健康文章
     *
     * @param saveReqVO 更新信息（后续替换为 HealthArticleSaveReqVO）
     */
    void updateHealthArticle(Object saveReqVO);

    /**
     * 删除健康文章
     *
     * @param id 编号
     */
    void deleteHealthArticle(Long id);

    /**
     * 获得健康文章
     *
     * @param id 编号
     * @return 健康文章
     */
    HealthArticleDO getHealthArticle(Long id);

    /**
     * 获得健康文章分页
     *
     * @param pageParam 分页查询（后续替换为 HealthArticlePageReqVO）
     * @return 健康文章分页
     */
    PageResult<HealthArticleDO> getHealthArticlePage(PageParam pageParam);

    /**
     * 定时发布：将到时间的待发布文章置为已发布
     *
     * @return 已发布文章数量
     */
    int publishArticle();

    /**
     * 置顶/取消置顶文章
     *
     * @param id 文章ID
     * @param isTop 是否置顶 0否 1是
     */
    void topArticle(Long id, Integer isTop);

    /**
     * 查询所有已发布文章（置顶优先）
     *
     * @return 已发布文章列表
     */
    List<HealthArticleDO> selectPublishedList();

}
