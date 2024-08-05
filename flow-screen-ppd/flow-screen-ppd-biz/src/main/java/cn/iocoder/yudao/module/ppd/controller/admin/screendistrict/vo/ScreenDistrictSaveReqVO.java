package cn.iocoder.yudao.module.ppd.controller.admin.screendistrict.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 甘孜州区划新增/修改 Request VO")
@Data
public class ScreenDistrictSaveReqVO {

    @Schema(description = "自增类型（主键）", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "区划代码（唯一）")
    private String code;

    @Schema(description = "区划级别")
    private String level;

    @Schema(description = "区划名称")
    private String name;

    @Schema(description = "上级地区code")
    private String parentCode;

}