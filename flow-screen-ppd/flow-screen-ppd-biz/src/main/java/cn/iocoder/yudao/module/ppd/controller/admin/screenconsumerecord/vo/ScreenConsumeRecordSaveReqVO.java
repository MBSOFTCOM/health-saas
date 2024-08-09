package cn.iocoder.yudao.module.ppd.controller.admin.screenconsumerecord.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 消耗管理记录新增/修改 Request VO")
@Data
public class ScreenConsumeRecordSaveReqVO {

    @Schema(description = "主键id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "变化量")
    private Integer changeNumber;

    @Schema(description = "变化类型（1：筛查自动扣减，2：手动增加库存，3：手动减少库存）")
    private Integer type;

    @Schema(description = "消耗管理表id", requiredMode = Schema.RequiredMode.REQUIRED, example = "811")
    private Long consumeId;

}