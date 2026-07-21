package cn.iocoder.yudao.module.childhealth.controller.admin.medical.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

@Schema(description = "管理后台 - 病历分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MedicalRecordPageReqVO extends PageParam {

    @Schema(description = "病历号", example = "MR20260720001")
    private String recordNo;

    @Schema(description = "儿童档案ID", example = "1001")
    private Long childId;

    @Schema(description = "学生ID", example = "2001")
    private Long studentId;

    @Schema(description = "病历模板ID", example = "10")
    private Long templateId;

    @Schema(description = "筛查批次ID", example = "5")
    private Long batchId;

    @Schema(description = "就诊类型 1满月 2常规体检 3入园入托 4转诊 5复诊", example = "2")
    private Integer visitType;

    @Schema(description = "医生ID", example = "500")
    private Long doctorId;

    @Schema(description = "审核状态 0未审核 1已审核 2已驳回", example = "0")
    private Integer auditStatus;

    @Schema(description = "就诊日期", example = "2026-07-20")
    private LocalDate visitDate;

}
