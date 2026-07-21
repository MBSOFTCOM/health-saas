package cn.iocoder.yudao.module.childhealth.service.medical;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.medical.DevelopmentAssessmentRecordDO;

/**
 * 发育评估记录表 Service 接口
 *
 * 模块: A. 儿童基础健康检查（A8-发育评估记录表）
 * 创建日期: 2026-07-20
 *
 * @author 芋道源码
 */
public interface DevelopmentAssessmentRecordService {

    /**
     * 创建发育评估记录
     *
     * @param saveReqVO 创建信息（后续替换为 DevelopmentAssessmentRecordSaveReqVO）
     * @return 编号
     */
    Long createDevelopmentAssessmentRecord(Object saveReqVO);

    /**
     * 更新发育评估记录
     *
     * @param saveReqVO 更新信息（后续替换为 DevelopmentAssessmentRecordSaveReqVO）
     */
    void updateDevelopmentAssessmentRecord(Object saveReqVO);

    /**
     * 删除发育评估记录
     *
     * @param id 编号
     */
    void deleteDevelopmentAssessmentRecord(Long id);

    /**
     * 获得发育评估记录
     *
     * @param id 编号
     * @return 发育评估记录
     */
    DevelopmentAssessmentRecordDO getDevelopmentAssessmentRecord(Long id);

    /**
     * 获得发育评估记录分页
     *
     * @param pageParam 分页查询（后续替换为 DevelopmentAssessmentRecordPageReqVO）
     * @return 发育评估记录分页
     */
    PageResult<DevelopmentAssessmentRecordDO> getDevelopmentAssessmentRecordPage(PageParam pageParam);

    /**
     * 自动计分：根据答题情况自动计算总分、各维度得分、风险等级
     *
     * @param recordId 评估记录ID
     */
    void autoCalculateScore(Long recordId);

    /**
     * 生成报告：根据评估结果生成 PDF 报告
     *
     * @param recordId 评估记录ID
     * @return 报告文件URL
     */
    String generateReport(Long recordId);

}
