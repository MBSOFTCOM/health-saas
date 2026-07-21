package cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.resultDetail;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 筛查结果明细分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ScreeningResultDetailPageReqVO extends PageParam {

    @Schema(description = "筛查记录ID", example = "1")
    private Long recordId;

    @Schema(description = "筛查项目编码", example = "VISION_LEFT")
    private String itemCode;

    @Schema(description = "是否异常 0正常 1异常", example = "0")
    private Integer isAbnormal;

    @Schema(description = "检查科室ID", example = "1")
    private Long deptId;

}