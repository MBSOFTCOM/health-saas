package cn.iocoder.yudao.module.childhealth.service.medical;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.medical.DevelopmentAssessmentAnswerDO;

import java.util.List;

/**
 * 发育评估答题表 Service 接口
 *
 * 模块: A. 儿童基础健康检查（A9-发育评估答题表）
 * 创建日期: 2026-07-20
 *
 * @author 芋道源码
 */
public interface DevelopmentAssessmentAnswerService {

    /**
     * 创建发育评估答题
     *
     * @param saveReqVO 创建信息（后续替换为 DevelopmentAssessmentAnswerSaveReqVO）
     * @return 编号
     */
    Long createDevelopmentAssessmentAnswer(Object saveReqVO);

    /**
     * 更新发育评估答题
     *
     * @param saveReqVO 更新信息（后续替换为 DevelopmentAssessmentAnswerSaveReqVO）
     */
    void updateDevelopmentAssessmentAnswer(Object saveReqVO);

    /**
     * 删除发育评估答题
     *
     * @param id 编号
     */
    void deleteDevelopmentAssessmentAnswer(Long id);

    /**
     * 获得发育评估答题
     *
     * @param id 编号
     * @return 发育评估答题
     */
    DevelopmentAssessmentAnswerDO getDevelopmentAssessmentAnswer(Long id);

    /**
     * 获得发育评估答题分页
     *
     * @param pageParam 分页查询（后续替换为 DevelopmentAssessmentAnswerPageReqVO）
     * @return 发育评估答题分页
     */
    PageResult<DevelopmentAssessmentAnswerDO> getDevelopmentAssessmentAnswerPage(PageParam pageParam);

    /**
     * 批量插入评估答题
     *
     * @param list 答题列表
     */
    void batchInsert(List<DevelopmentAssessmentAnswerDO> list);

    /**
     * 按评估记录ID查询所有答题
     *
     * @param recordId 评估记录ID
     * @return 答题列表
     */
    List<DevelopmentAssessmentAnswerDO> selectListByRecordId(Long recordId);

}
