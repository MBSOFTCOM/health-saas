package cn.iocoder.yudao.module.childhealth.controller.admin.medical.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

@Schema(description = "管理后台 - 发育评估记录分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DevelopmentAssessmentRecordPageReqVO extends PageParam {

    @Schema(description = "量表ID", example = "10")
    private Long scaleId;

    @Schema(description = "儿童ID", example = "1001")
    private Long childId;

    @Schema(description = "关联病历ID", example = "100")
    private Long medicalRecordId;

    @Schema(description = "评估人ID（医生/家长）", example = "500")
    private Long assessorId;

    @Schema(description = "评估人类型 1医生 2家长", example = "1")
    private Integer assessorType;

    @Schema(description = "评估日期", example = "2026-07-20")
    private LocalDate assessDate;

    @Schema(description = "风险等级 1正常 2临界 3异常", example = "1")
    private Integer riskLevel;

    @Schema(description = "状态 0进行中 1已完成 2已废弃", example = "0")
    private Integer status;

}
