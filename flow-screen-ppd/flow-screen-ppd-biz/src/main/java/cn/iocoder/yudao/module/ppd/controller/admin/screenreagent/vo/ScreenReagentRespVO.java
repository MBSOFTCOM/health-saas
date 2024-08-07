package cn.iocoder.yudao.module.ppd.controller.admin.screenreagent.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 试剂 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ScreenReagentRespVO {

    @Schema(description = "试剂名称")
    @ExcelProperty("试剂名称")
    private String name;

    @Schema(description = "试剂类型")
    @ExcelProperty("试剂类型")
    private Integer type;

    @Schema(description = "转换系数（人次）")
    @ExcelProperty("转换系数（人次）")
    private Integer reagentSpecsNum;

    @Schema(description = "是否启用")
    @ExcelProperty("是否启用")
    private Integer usable;

    @Schema(description = "效价")
    @ExcelProperty("效价")
    private BigDecimal titer;

    @Schema(description = "效价单位")
    @ExcelProperty("效价单位")
    private Integer potencyUnit;

    @Schema(description = "规格")
    @ExcelProperty("规格")
    private BigDecimal specification;

    @Schema(description = "规格单位")
    @ExcelProperty("规格单位")
    private Integer specificationUnit;

    @Schema(description = "包装单位")
    @ExcelProperty("包装单位")
    private Integer packageUnit;

    @Schema(description = "供应商")
    @ExcelProperty("供应商")
    private String manufacturer;

    @Schema(description = "库存预警值（按试剂）")
    @ExcelProperty("库存预警值（按试剂）")
    private Integer threshold;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "自增主键id", requiredMode = Schema.RequiredMode.REQUIRED, example = "3319")
    @ExcelProperty("自增主键id")
    private Long id;

}