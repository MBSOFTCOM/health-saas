package cn.iocoder.yudao.module.ppd.controller.admin.screensum.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 汇总分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ScreenSumPageReqVO extends PageParam {

    @Schema(description = "筛查年份")
    private String year;

    @Schema(description = "筛查类型")
    private Integer screenType;

    @Schema(description = "身份证")
    private String idNum;

    @Schema(description = "对应摸底表中id", example = "4623")
    private Long personId;

    @Schema(description = "同步时唯一编码", example = "8496")
    private Long syncId;

    @Schema(description = "采集表id", example = "2644")
    private Long collectId;

    @Schema(description = "ppd表id", example = "4570")
    private Long ppdId;

    @Schema(description = "dr胸片表id", example = "10663")
    private Long chestRadiographId;

    @Schema(description = "ct胸片表id", example = "10663")
    private Long computedTomographyId;


}