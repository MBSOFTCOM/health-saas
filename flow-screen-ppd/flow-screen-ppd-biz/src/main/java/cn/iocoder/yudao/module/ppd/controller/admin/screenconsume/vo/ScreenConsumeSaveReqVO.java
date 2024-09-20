package cn.iocoder.yudao.module.ppd.controller.admin.screenconsume.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 消耗管理新增/修改 Request VO")
@Data
public class ScreenConsumeSaveReqVO {

    @Schema(description = "主键id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "试剂id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "试剂id不能为空")
    private Long reagentId;

    @Schema(description = "试剂名称")
    private String reagentName;

    @Schema(description = "试剂类型")
    private Integer reagentType;

    @Schema(description = "消耗序位")
    @NotNull
    @Max(99)
    @Min(1)
    private Integer consumeOrder;

    @Schema(description = "批次号")
    private String bathNumber;

    @Schema(description = "入库量（按试剂）")
    private Integer inboundNumber;

    @Schema(description = "当前库存")
    private Integer currentNumber;

    @Schema(description = "生产日期")
    private LocalDateTime manufactureDate;

    @Schema(description = "转换系数（人次）")
    @Min(1)
    @NotNull
    private Integer reagentSpecsNum;

    @Schema(description = "库存预警值（按试剂）")
    private Integer threshold;

    @Schema(description = "是否启用")
    private Integer usable;

    @Schema(description = "有效期")
    private String indate;

}