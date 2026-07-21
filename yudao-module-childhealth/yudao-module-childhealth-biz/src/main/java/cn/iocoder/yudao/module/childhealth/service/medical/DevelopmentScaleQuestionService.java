package cn.iocoder.yudao.module.childhealth.service.medical;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.medical.DevelopmentScaleQuestionDO;

import java.util.List;

/**
 * 发育评估量表题目表 Service 接口
 *
 * 模块: A. 儿童基础健康检查（A7-发育评估量表题目表）
 * 创建日期: 2026-07-20
 *
 * @author 芋道源码
 */
public interface DevelopmentScaleQuestionService {

    /**
     * 创建量表题目
     *
     * @param saveReqVO 创建信息（后续替换为 DevelopmentScaleQuestionSaveReqVO）
     * @return 编号
     */
    Long createDevelopmentScaleQuestion(Object saveReqVO);

    /**
     * 更新量表题目
     *
     * @param saveReqVO 更新信息（后续替换为 DevelopmentScaleQuestionSaveReqVO）
     */
    void updateDevelopmentScaleQuestion(Object saveReqVO);

    /**
     * 删除量表题目
     *
     * @param id 编号
     */
    void deleteDevelopmentScaleQuestion(Long id);

    /**
     * 获得量表题目
     *
     * @param id 编号
     * @return 量表题目
     */
    DevelopmentScaleQuestionDO getDevelopmentScaleQuestion(Long id);

    /**
     * 获得量表题目分页
     *
     * @param pageParam 分页查询（后续替换为 DevelopmentScaleQuestionPageReqVO）
     * @return 量表题目分页
     */
    PageResult<DevelopmentScaleQuestionDO> getDevelopmentScaleQuestionPage(PageParam pageParam);

    /**
     * 按量表ID查询所有题目
     *
     * @param scaleId 量表ID
     * @return 题目列表
     */
    List<DevelopmentScaleQuestionDO> selectListByScaleId(Long scaleId);

    /**
     * 批量插入量表题目
     *
     * @param list 题目列表
     */
    void batchInsert(List<DevelopmentScaleQuestionDO> list);

}
