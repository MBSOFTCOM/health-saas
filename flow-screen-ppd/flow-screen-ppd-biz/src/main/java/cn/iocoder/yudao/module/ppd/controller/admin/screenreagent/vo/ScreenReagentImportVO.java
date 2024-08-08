package cn.iocoder.yudao.module.ppd.controller.admin.screenreagent.vo;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Stream;

@Schema(description = "管理后台 - 试剂 Response VO")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = false) // 设置 chain = false，避免用户导入有问题
@ExcelIgnoreUnannotated
public class ScreenReagentImportVO {

    @Schema(description = "试剂名称")
    @ExcelProperty("试剂名称")
    private String name;

    @Schema(description = "试剂类型")
    @ExcelProperty(value = "试剂类型", converter = DictConvert.class)
    @DictFormat("dosage_form")
    private Integer type;

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

    public boolean isEmpty(ScreenReagentImportVO respVO) {
        ScreenReagentImportVO vo = new ScreenReagentImportVO();
        return vo.equals(respVO);
    }
}