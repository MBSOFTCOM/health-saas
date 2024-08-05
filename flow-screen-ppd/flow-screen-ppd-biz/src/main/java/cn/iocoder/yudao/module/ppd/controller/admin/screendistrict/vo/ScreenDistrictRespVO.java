package cn.iocoder.yudao.module.ppd.controller.admin.screendistrict.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 甘孜州区划 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ScreenDistrictRespVO {

    @Schema(description = "自增类型（主键）", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("自增类型（主键）")
    private Long id;

    @Schema(description = "区划代码（唯一）")
    @ExcelProperty("区划代码（唯一）")
    private String code;

    @Schema(description = "区划级别")
    @ExcelProperty("区划级别")
    private String level;

    @Schema(description = "区划名称")
    @ExcelProperty("区划名称")
    private String name;

    @Schema(description = "上级地区code")
    @ExcelProperty("上级地区code")
    private String parentCode;

}