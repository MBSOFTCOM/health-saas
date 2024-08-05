package cn.iocoder.yudao.module.ppd.controller.admin.synchronization.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ScreenPointVO {
    @Schema(description = "筛查点id")
    private Long screenPointId;

    @Schema(description = "筛查点名称")
    private String name;

    @Schema(description = "筛查单位")
    private String screenDept;

}
