package cn.iocoder.yudao.module.childhealth.controller.admin.medical.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 病历新增/修改 Request VO")
@Data
public class MedicalRecordSaveReqVO {

    @Schema(description = "主键ID", example = "1")
    private Long id;

    @Schema(description = "病历号", requiredMode = Schema.RequiredMode.REQUIRED, example = "MR20260720001")
    @NotBlank(message = "病历号不能为空")
    private String recordNo;

    @Schema(description = "儿童档案ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1001")
    private Long childId;

    @Schema(description = "学生ID", example = "2001")
    private Long studentId;

    @Schema(description = "病历模板ID", example = "10")
    private Long templateId;

    @Schema(description = "筛查批次ID", example = "5")
    private Long batchId;

    @Schema(description = "就诊日期", example = "2026-07-20")
    private LocalDate visitDate;

    @Schema(description = "就诊类型 1满月 2常规体检 3入园入托 4转诊 5复诊", example = "2")
    private Integer visitType;

    @Schema(description = "月龄", example = "12")
    private Integer ageMonth;

    @Schema(description = "主诉")
    private String chiefComplaint;

    @Schema(description = "现病史")
    private String presentIllness;

    @Schema(description = "既往史")
    private String pastHistory;

    @Schema(description = "体格检查")
    private String physicalExam;

    @Schema(description = "专科检查")
    private String specialistExam;

    @Schema(description = "辅助检查")
    private String auxExam;

    @Schema(description = "诊断")
    private String diagnosis;

    @Schema(description = "健康指导")
    private String healthGuidance;

    @Schema(description = "下次就诊建议")
    private String nextVisitAdvice;

    @Schema(description = "医生ID", example = "500")
    private Long doctorId;

    @Schema(description = "医生姓名", example = "张医生")
    private String doctorName;

    @Schema(description = "科室ID", example = "100")
    private Long deptId;

    @Schema(description = "专案提醒状态 0未提醒 1已提醒建专案", example = "0")
    private Integer caseRemindStatus;

    @Schema(description = "审核状态 0未审核 1已审核 2已驳回", example = "0")
    private Integer auditStatus;

    @Schema(description = "审核医生ID", example = "501")
    private Long auditDoctorId;

    @Schema(description = "审核时间")
    private LocalDateTime auditTime;

}
