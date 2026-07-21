package cn.iocoder.yudao.module.childhealth.controller.admin.casemanagement.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 高危新生儿台账 Response VO")
@Data
public class HighRiskNewbornRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "儿童档案ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long childId;

    @Schema(description = "母亲姓名", example = "李梅")
    private String motherName;

    @Schema(description = "母亲年龄", example = "28")
    private Integer motherAge;

    @Schema(description = "孕周", example = "36")
    private Integer pregnancyWeek;

    @Schema(description = "孕期高危因素")
    private String pregnancyRisk;

    @Schema(description = "出生体重(kg)", example = "2.5")
    private BigDecimal birthWeight;

    @Schema(description = "出生身长(cm)", example = "47.0")
    private BigDecimal birthLength;

    @Schema(description = "Apgar 1分钟评分", example = "8")
    private Integer apgarScore1min;

    @Schema(description = "Apgar 5分钟评分", example = "10")
    private Integer apgarScore5min;

    @Schema(description = "高危类型JSON（早产/低体重/高胆红素血症/遗传代谢病/HIE）")
    private String highRiskTypes;

    @Schema(description = "是否已建册 0否 1是", example = "1")
    private Integer isRegistered;

    @Schema(description = "建册日期")
    private LocalDate registerDate;

    @Schema(description = "建册机构", example = "XX妇幼保健院")
    private String registerOrg;

    @Schema(description = "随访状态 0未随访 1随访中 2已结案", example = "0")
    private Integer followupStatus;

    @Schema(description = "关联专案ID", example = "1")
    private Long caseId;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
