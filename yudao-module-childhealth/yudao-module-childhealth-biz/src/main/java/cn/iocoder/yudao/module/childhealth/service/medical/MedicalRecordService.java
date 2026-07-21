package cn.iocoder.yudao.module.childhealth.service.medical;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.medical.MedicalRecordDO;

import java.time.LocalDate;

/**
 * 病历主表 Service 接口
 *
 * 模块: A. 儿童基础健康检查（A2-病历主表）
 * 创建日期: 2026-07-20
 *
 * @author 芋道源码
 */
public interface MedicalRecordService {

    /**
     * 创建病历
     *
     * @param saveReqVO 创建信息（后续替换为 MedicalRecordSaveReqVO）
     * @return 编号
     */
    Long createMedicalRecord(Object saveReqVO);

    /**
     * 更新病历
     *
     * @param saveReqVO 更新信息（后续替换为 MedicalRecordSaveReqVO）
     */
    void updateMedicalRecord(Object saveReqVO);

    /**
     * 删除病历
     *
     * @param id 编号
     */
    void deleteMedicalRecord(Long id);

    /**
     * 获得病历
     *
     * @param id 编号
     * @return 病历
     */
    MedicalRecordDO getMedicalRecord(Long id);

    /**
     * 获得病历分页
     *
     * @param pageParam 分页查询（后续替换为 MedicalRecordPageReqVO）
     * @return 病历分页
     */
    PageResult<MedicalRecordDO> getMedicalRecordPage(PageParam pageParam);

    /**
     * 按病历号查询
     *
     * @param recordNo 病历号
     * @return 病历
     */
    MedicalRecordDO selectByRecordNo(String recordNo);

    /**
     * 一键填充：根据模板默认值填充病历字段
     *
     * @param recordId 病历ID
     * @param templateId 模板ID
     */
    void autoFillFromTemplate(Long recordId, Long templateId);

    /**
     * 当日数据归集到病历（汇总检查/评估结果到病历主表）
     *
     * @param childId 儿童档案ID
     * @param visitDate 就诊日期
     */
    void aggregateToMedicalRecord(Long childId, LocalDate visitDate);

}
