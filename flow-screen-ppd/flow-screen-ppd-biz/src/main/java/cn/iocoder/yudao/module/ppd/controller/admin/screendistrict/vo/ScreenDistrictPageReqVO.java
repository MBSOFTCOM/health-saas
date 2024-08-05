package cn.iocoder.yudao.module.ppd.controller.admin.screendistrict.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 甘孜州区划分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ScreenDistrictPageReqVO extends PageParam {

    @Schema(description = "区划代码（唯一）")
    private String code;

    @Schema(description = "区划级别")
    private String level;

    @Schema(description = "区划名称")
    private String name;

    @Schema(description = "上级地区code")
    private String parentCode;

}