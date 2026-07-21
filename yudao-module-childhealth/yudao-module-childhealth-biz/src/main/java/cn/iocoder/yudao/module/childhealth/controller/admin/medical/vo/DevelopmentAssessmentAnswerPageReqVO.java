package cn.iocoder.yudao.module.childhealth.controller.admin.medical.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 发育评估答题分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DevelopmentAssessmentAnswerPageReqVO extends PageParam {

    @Schema(description = "评估记录ID", example = "100")
    private Long recordId;

    @Schema(description = "题目ID", example = "10")
    private Long questionId;

}
