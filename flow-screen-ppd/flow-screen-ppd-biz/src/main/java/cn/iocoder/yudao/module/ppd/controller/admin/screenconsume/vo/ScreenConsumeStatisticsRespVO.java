package cn.iocoder.yudao.module.ppd.controller.admin.screenconsume.vo;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ExcelIgnoreUnannotated
public class ScreenConsumeStatisticsRespVO {

    @Schema(description = "试剂名称")
    @ExcelProperty("试剂名称")
    private String reagentName;

    @Schema(description = "批次号")
    @ExcelProperty("批次号")
    private String bathNumber;

    @Schema(description = "库存消耗量")
    @ExcelProperty("库存消耗量")
    private Integer consumption;

    @Schema(description = "消耗量同比")
    @ExcelProperty("消耗量同比(%)")
    private Double consumptionPercentage;
}