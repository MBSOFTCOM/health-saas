package cn.iocoder.yudao.module.childhealth.controller.app.parent.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * 家长 App - 阳性结果与健康指导 VO
 *
 * 创建日期: 2026-07-20
 * 模块: 移动端 - 家长 App
 */
@Schema(description = "家长 App - 阳性结果 VO")
@Data
public class ParentPositiveRespVO {

    @Schema(description = "阳性记录ID", example = "2001")
    private Long positiveId;

    @Schema(description = "筛查记录ID", example = "5001")
    private Long recordId;

    @Schema(description = "学生ID", example = "1001")
    private Long studentId;

    @Schema(description = "疾病编码", example = "H52.0")
    private String diseaseCode;

    @Schema(description = "疾病名称", example = "近视")
    private String diseaseName;

    @Schema(description = "阳性等级 1轻度 2中度 3重度", example = "1")
    private Integer positiveLevel;

    @Schema(description = "阳性指标JSON")
    private String positiveItems;

    @Schema(description = "健康指导")
    private String healthGuidance;

    @Schema(description = "是否需要复筛 0否 1是", example = "1")
    private Integer needRecheck;

    @Schema(description = "复筛状态 0未通知 1通知已入队 2已完成", example = "1")
    private Integer recheckStatus;

    @Schema(description = "复筛建议日期")
    private LocalDate suggestRecheckDate;

    @Schema(description = "复筛建议医院")
    private String suggestHospital;

    @Schema(description = "复筛建议科室")
    private String suggestDepartment;

}
