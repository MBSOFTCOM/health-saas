package cn.iocoder.yudao.module.childhealth.controller.admin.exam.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 口腔检查录入请求 VO
 */
@Schema(description = "口腔检查录入请求")
@Data
public class OralExamCreateReqVO {

    @Schema(description = "体检记录ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long examId;

    @Schema(description = "牙齿数量")
    private Integer toothCount;

    @Schema(description = "龋齿数")
    private Integer cariesCount;

    @Schema(description = "龋齿类型")
    private String cariesType;

    @Schema(description = "牙龈情况")
    private String gumCondition;

    @Schema(description = "口腔卫生")
    private String oralHygiene;

    @Schema(description = "咬合异常")
    private String malocclusion;

    @Schema(description = "诊断")
    private String diagnosis;
}