package cn.iocoder.yudao.module.ppd.controller.admin.synchronization.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PageReqVO extends PageParam {
    @Schema(description = "每页条数")
    private Integer pageSize;

    @Schema(description = "页码")
    private Integer pageNo;

    @Schema(description = "筛查编号")
    private String screenId;

    @Schema(description = "筛查点")
    private String screenPoint;

    @Schema(description = "是否注射(1-是 0-否)")
    private Boolean injection;


}
