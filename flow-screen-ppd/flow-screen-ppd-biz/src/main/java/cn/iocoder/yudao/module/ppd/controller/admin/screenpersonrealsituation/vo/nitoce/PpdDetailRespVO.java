package cn.iocoder.yudao.module.ppd.controller.admin.screenpersonrealsituation.vo.nitoce;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PpdDetailRespVO {
    private Long id;
    private String idNum;
    private Integer outcome;
    private String bleb;
    private Integer transverseDiameter;
    private Integer longitudinalDiameter;
    private Integer blushTransverseDiameter;
    private Integer blushLongitudinalDiameter;
}
