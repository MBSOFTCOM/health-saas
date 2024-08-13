package cn.iocoder.yudao.module.ppd.controller.admin.screenreagent.vo;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 试剂 Response VO")
@Data
@Builder
@ExcelIgnoreUnannotated
public class ScreenReagentRespVO {

    @Schema(description = "试剂名称")
    @ExcelProperty("试剂名称")
    private String name;

    @Schema(description = "试剂类型")
    @ExcelProperty(value = "试剂类型", converter = DictConvert.class)
    @DictFormat("dosage_form")
    private Integer type;

    @Schema(description = "是否启用")
    @ExcelProperty(value = "是否启用", converter = DictConvert.class)
    @DictFormat("common_status")
    private Integer usable;

    @Schema(description = "转换系数（人次）")
    @ExcelProperty("转换系数（人次）")
    private Integer reagentSpecsNum;

    @Schema(description = "效价")
    @ExcelProperty("效价")
    private BigDecimal titer;

    @Schema(description = "效价单位")
    @ExcelProperty(value = "效价单位", converter = DictConvert.class)
    @DictFormat("tb_potency_unit")
    private Integer potencyUnit;

    @Schema(description = "规格")
    @ExcelProperty("规格")
    private BigDecimal specification;

    @Schema(description = "规格单位")
    @ExcelProperty(value = "规格单位", converter = DictConvert.class)
    @DictFormat("tb_specification")
    private Integer specificationUnit;

    @Schema(description = "包装单位")
    @ExcelProperty(value = "包装单位", converter = DictConvert.class)
    @DictFormat("tb_package")
    private Integer packageUnit;

    @Schema(description = "库存预警值（按试剂）")
    @ExcelProperty("库存预警值")
    private Integer threshold;

    @Schema(description = "供应商")
    @ExcelProperty("供应商")
    private String manufacturer;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "自增主键id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "部门id")
    private Long deptId;

}