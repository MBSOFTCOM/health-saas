package cn.iocoder.yudao.module.childhealth.controller.admin.casemanagement.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 高危儿随访 Response VO")
@Data
public class HighRiskFollowupRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "高危新生儿ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long newbornId;

    @Schema(description = "儿童ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long childId;

    @Schema(description = "随访编号", example = "HF20260720001")
    private String followupNo;

    @Schema(description = "随访日期")
    private LocalDate followupDate;

    @Schema(description = "随访类型 1院内 2家庭 3电话", example = "1")
    private Integer followupType;

    @Schema(description = "月龄", example = "1")
    private Integer ageMonth;

    @Schema(description = "体重(kg)", example = "3.5")
    private BigDecimal bodyWeight;

    @Schema(description = "身长(cm)", example = "50.0")
    private BigDecimal bodyLength;

    @Schema(description = "头围(cm)", example = "34.0")
    private BigDecimal headCircumference;

    @Schema(description = "体征数据JSON")
    private String bodyDataJson;

    @Schema(description = "发育状态")
    private String developmentStatus;

    @Schema(description = "喂养情况")
    private String feedingStatus;

    @Schema(description = "营养状况")
    private String nutritionStatus;

    @Schema(description = "医生ID", example = "1")
    private Long doctorId;

    @Schema(description = "医生姓名", example = "张医生")
    private String doctorName;

    @Schema(description = "下次随访日期")
    private LocalDate nextFollowupDate;

    @Schema(description = "下次随访月龄", example = "3")
    private Integer nextFollowupAgeMonth;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
