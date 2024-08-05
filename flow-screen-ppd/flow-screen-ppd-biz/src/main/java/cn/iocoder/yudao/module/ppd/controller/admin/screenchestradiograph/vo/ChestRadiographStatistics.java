package cn.iocoder.yudao.module.ppd.controller.admin.screenchestradiograph.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChestRadiographStatistics {
    private Integer normal;
    private Integer suspected;
    private Integer dayNum;
    private Integer yearNum;
    private Integer monthNum;
}
