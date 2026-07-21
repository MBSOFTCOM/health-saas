package cn.iocoder.yudao.module.childhealth.controller.admin.medical.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 病历模板 Response VO")
@Data
public class MedicalRecordTemplateRespVO {

    @Schema(description = "主键ID", example = "1")
    private Long id;

    @Schema(description = "模板编码", example = "EYE-01M")
    private String templateCode;

    @Schema(description = "模板名称", example = "满月眼保健病历模板")
    private String templateName;

    @Schema(description = "模板类型 GENERAL_CHECKUP/EYE_HEALTH/HEARING_HEALTH/ORAL_HEALTH/ENTRY_EXAM", example = "EYE_HEALTH")
    private String templateType;

    @Schema(description = "适用最小月龄", example = "0")
    private Integer ageMonthMin;

    @Schema(description = "适用最大月龄", example = "1")
    private Integer ageMonthMax;

    @Schema(description = "所依据的国家规范")
    private String normVersion;

    @Schema(description = "模板说明")
    private String description;

    @Schema(description = "模板内容结构化JSON")
    private String templateContent;

    @Schema(description = "字段映射关系JSON")
    private String fieldMapping;

    @Schema(description = "异常判定阈值JSON")
    private String abnormalityThresholds;

    @Schema(description = "健康指导JSON")
    private String healthGuidance;

    @Schema(description = "随访建议JSON")
    private String followUpRecommendation;

    @Schema(description = "排序")
    private Integer sortOrder;

    @Schema(description = "状态 1启用 0停用", example = "1")
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
