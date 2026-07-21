package cn.iocoder.yudao.module.childhealth.controller.admin.screeningconfig.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 体检方案与批次关联分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ScreeningPlanBatchPageReqVO extends PageParam {

    @Schema(description = "方案ID", example = "1")
    private Long planId;

    @Schema(description = "批次ID", example = "100")
    private Long batchId;

    @Schema(description = "学校ID", example = "10")
    private Long schoolId;

    @Schema(description = "年级ID", example = "20")
    private Long gradeId;

    @Schema(description = "班级ID", example = "30")
    private Long classId;

    @Schema(description = "完成状态 0未开始 1进行中 2已完成", example = "0")
    private Integer completionStatus;

}
