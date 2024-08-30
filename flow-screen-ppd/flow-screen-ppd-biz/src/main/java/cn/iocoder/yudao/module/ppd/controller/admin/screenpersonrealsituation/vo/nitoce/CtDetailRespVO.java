package cn.iocoder.yudao.module.ppd.controller.admin.screenpersonrealsituation.vo.nitoce;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CtDetailRespVO {
    private Long id;
    private String computedTomographyCode;
    /**
     * 结果。2-其他异常 1-疑似结核 0-无异常
     */
    private Integer outcome;
    private String remark;
}
