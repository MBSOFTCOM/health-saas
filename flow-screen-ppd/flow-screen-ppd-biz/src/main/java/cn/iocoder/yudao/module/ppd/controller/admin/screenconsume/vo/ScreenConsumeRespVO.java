package cn.iocoder.yudao.module.ppd.controller.admin.screenconsume.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 消耗管理 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ScreenConsumeRespVO {

    @Schema(description = "主键id", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("主键id")
    private Long id;

    @Schema(description = "试剂id", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("试剂id")
    private Long reagentId;

    @Schema(description = "试剂名称")
    @ExcelProperty("试剂名称")
    private String reagentName;

    @Schema(description = "试剂类型")
    @ExcelProperty("试剂类型")
    private Integer reagentType;

    @Schema(description = "消耗序位")
    @ExcelProperty("消耗序位")
    private Integer consumeOrder;

    @Schema(description = "批次号")
    @ExcelProperty("批次号")
    private String bathNumber;

    @Schema(description = "入库量（按试剂）")
    @ExcelProperty("入库量（按试剂）")
    private Integer inboundNumber;

    @Schema(description = "生产日期")
    @ExcelProperty("生产日期")
    private LocalDateTime manufactureDate;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "转换系数（人次）")
    @ExcelProperty("转换系数（人次）")
    private Integer reagentSpecsNum;

    @Schema(description = "库存预警值（按试剂）")
    @ExcelProperty("库存预警值（按试剂）")
    private Integer threshold;

    @Schema(description = "是否启用")
    @ExcelProperty("是否启用")
    private Integer usable;

    @Schema(description = "有效期")
    @ExcelProperty("有效期")
    private String indate;

}