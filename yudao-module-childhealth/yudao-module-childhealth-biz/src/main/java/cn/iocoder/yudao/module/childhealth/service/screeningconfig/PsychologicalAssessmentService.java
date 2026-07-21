package cn.iocoder.yudao.module.childhealth.service.screeningconfig;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screeningconfig.PsychologicalAssessmentDO;

import java.util.List;

/**
 * 心理量表评估记录 Service 接口
 *
 * 模块: C.五健筛查配置
 * 创建日期: 2026-07-20
 *
 * @author 芋道源码
 */
public interface PsychologicalAssessmentService {

    /**
     * 创建心理评估记录
     *
     * @param saveReqVO 创建信息（后续替换为 PsychologicalAssessmentSaveReqVO）
     * @return 编号
     */
    Long createPsychologicalAssessment(Object saveReqVO);

    /**
     * 更新心理评估记录
     *
     * @param saveReqVO 更新信息（后续替换为 PsychologicalAssessmentSaveReqVO）
     */
    void updatePsychologicalAssessment(Object saveReqVO);

    /**
     * 删除心理评估记录
     *
     * @param id 编号
     */
    void deletePsychologicalAssessment(Long id);

    /**
     * 获得心理评估记录
     *
     * @param id 编号
     * @return 心理评估记录
     */
    PsychologicalAssessmentDO getPsychologicalAssessment(Long id);

    /**
     * 获得心理评估记录分页
     *
     * @param pageParam 分页查询（后续替换为 PsychologicalAssessmentPageReqVO）
     * @return 心理评估记录分页
     */
    PageResult<PsychologicalAssessmentDO> getPsychologicalAssessmentPage(PageParam pageParam);

    /**
     * 自动计分：根据量表题目与答题明细计算总分、各维度得分，回写评估记录
     *
     * @param id 评估记录ID
     */
    void autoCalculateScore(Long id);

    /**
     * 自动风险分级：根据总分及量表临界值配置，确定风险等级，回写评估记录
     *
     * @param id 评估记录ID
     */
    void autoRiskLevel(Long id);

    /**
     * 按儿童ID及量表ID查询评估记录
     *
     * @param childId 儿童ID
     * @param scaleId 量表ID
     * @return 评估记录列表
     */
    List<PsychologicalAssessmentDO> selectByChildAndScale(Long childId, Long scaleId);

}
