package cn.iocoder.yudao.module.ppd.controller.admin.screensum.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ScreenSumInfo {
    @Schema(description = "id")
    private Long id;

    @Schema(description = "当前已完成的分组")
    private String curFinish;

}
