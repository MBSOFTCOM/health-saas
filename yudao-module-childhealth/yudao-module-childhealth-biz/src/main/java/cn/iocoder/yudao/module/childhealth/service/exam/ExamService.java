package cn.iocoder.yudao.module.childhealth.service.exam;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.controller.admin.exam.vo.*;
import cn.iocoder.yudao.module.childhealth.controller.admin.medical.vo.MedicalRecordTemplatePageReqVO;
import cn.iocoder.yudao.module.childhealth.controller.admin.medical.vo.MedicalRecordTemplateRespVO;

/**
 * 体检业务 Service 接口
 */
public interface ExamService {

    // ==================== 预约管理 ====================

    /**
     * 创建体检预约
     */
    Long createAppointment(ExamAppointmentCreateReqVO reqVO);

    /**
     * 更新预约状态
     */
    void updateAppointmentStatus(Long id, Integer status);

    /**
     * 获取预约分页列表
     */
    PageResult<ExamAppointmentRespVO> getAppointmentPage(ExamAppointmentPageReqVO reqVO);

    /**
     * 获取预约详情
     */
    ExamAppointmentRespVO getAppointment(Long id);

    /**
     * 取消预约
     */
    void cancelAppointment(Long id);

    // ==================== 体检记录管理 ====================

    /**
     * 创建体检记录
     */
    Long createExamRecord(ExamRecordCreateReqVO reqVO);

    /**
     * 获取体检记录分页列表
     */
    PageResult<ExamRecordRespVO> getExamRecordPage(ExamRecordPageReqVO reqVO);

    /**
     * 获取体检记录详情
     */
    ExamRecordRespVO getExamRecord(Long id);

    /**
     * 提交体检记录审核
     */
    void submitExamRecord(Long id);

    /**
     * 审核体检记录
     */
    void reviewExamRecord(Long id, Boolean approved);

    // ==================== 体格检查 ====================

    /**
     * 录入体格检查数据（自动识别异常）
     */
    Long createPhysicalExam(PhysicalExamCreateReqVO reqVO);

    /**
     * 更新体格检查数据
     */
    void updatePhysicalExam(Long id, PhysicalExamCreateReqVO reqVO);

    // ==================== 专科检查 ====================

    /**
     * 录入眼保健检查数据（自动识别异常）
     */
    Long createEyeExam(EyeExamCreateReqVO reqVO);

    /**
     * 录入听力检查数据（自动识别异常）
     */
    Long createHearingExam(HearingExamCreateReqVO reqVO);

    /**
     * 录入口腔检查数据（自动识别异常）
     */
    Long createOralExam(OralExamCreateReqVO reqVO);

    // ==================== 辅助检查报告 ====================

    /**
     * 录入辅助检查报告（LIS/PACS/手工）
     */
    Long createLabReport(LabReportCreateReqVO reqVO);

    /**
     * 获取体检记录的辅助检查报告列表
     */
    java.util.List<LabReportRespVO> getLabReportsByExamId(Long examId);

    // ==================== 病历模板 ====================

    /**
     * 根据类型和月龄获取适用的病历模板
     */
    MedicalRecordTemplateRespVO getApplicableTemplate(String templateType, Integer monthAge);

    /**
     * 获取病历模板分页列表
     */
    PageResult<MedicalRecordTemplateRespVO> getTemplatePage(MedicalRecordTemplatePageReqVO reqVO);

    /**
     * 获取病历模板详情
     */
    MedicalRecordTemplateRespVO getTemplate(Long id);

    // ==================== 公卫保健计划 ====================

    /**
     * 根据儿童出生日期和首次就诊日期自动生成公卫儿童保健计划
     *
     * @param childId 儿童ID
     * @return 保健计划ID
     */
    Long generateCarePlan(Long childId);

    /**
     * 手动添加单次预约
     *
     * @param reqVO 预约创建请求
     * @return 预约ID
     */
    Long addManualAppointment(ExamAppointmentCreateReqVO reqVO);

    /**
     * 获取儿童的保健计划列表
     *
     * @param childId 儿童ID
     * @return 保健计划列表
     */
    java.util.List<HealthCarePlanRespVO> getCarePlansByChildId(Long childId);

    /**
     * 获取保健计划下的预约列表
     *
     * @param planId 计划ID
     * @return 预约列表
     */
    java.util.List<ExamAppointmentRespVO> getAppointmentsByPlanId(Long planId);
}