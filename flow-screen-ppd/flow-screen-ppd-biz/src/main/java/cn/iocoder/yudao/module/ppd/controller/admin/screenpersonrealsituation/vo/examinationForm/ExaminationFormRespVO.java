package cn.iocoder.yudao.module.ppd.controller.admin.screenpersonrealsituation.vo.examinationForm;

import cn.iocoder.yudao.module.ppd.controller.admin.screenpersonrealsituation.vo.nitoce.CtDetailRespVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenpersonrealsituation.vo.nitoce.PpdDetailRespVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExaminationFormRespVO {
    private CtDetailRespVO ctDetail;
    private CtDetailRespVO drDetail;
    private PpdDetailRespVO ppdDetail;
}
