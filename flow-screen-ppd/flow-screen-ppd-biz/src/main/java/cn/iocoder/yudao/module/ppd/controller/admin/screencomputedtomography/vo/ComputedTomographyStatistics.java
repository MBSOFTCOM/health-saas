package cn.iocoder.yudao.module.ppd.controller.admin.screencomputedtomography.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComputedTomographyStatistics {
    private Integer normal;
    private Integer suspected;
    private Integer dayNum;
    private Integer yearNum;
    private Integer monthNum;
}
