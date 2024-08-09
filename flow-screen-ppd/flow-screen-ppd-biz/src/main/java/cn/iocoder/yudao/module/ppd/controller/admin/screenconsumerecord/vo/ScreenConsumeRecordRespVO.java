package cn.iocoder.yudao.module.ppd.controller.admin.screenconsumerecord.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 消耗管理记录 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ScreenConsumeRecordRespVO {

    @Schema(description = "主键id", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("主键id")
    private Long id;

    @Schema(description = "变化量")
    @ExcelProperty("变化量")
    private Integer changeNumber;

    @Schema(description = "变化类型（1：筛查自动扣减，2：手动增加库存，3：手动减少库存）")
    @ExcelProperty("变化类型（1：筛查自动扣减，2：手动增加库存，3：手动减少库存）")
    private Integer type;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "消耗管理表id", requiredMode = Schema.RequiredMode.REQUIRED, example = "811")
    @ExcelProperty("消耗管理表id")
    private Long consumeId;

}