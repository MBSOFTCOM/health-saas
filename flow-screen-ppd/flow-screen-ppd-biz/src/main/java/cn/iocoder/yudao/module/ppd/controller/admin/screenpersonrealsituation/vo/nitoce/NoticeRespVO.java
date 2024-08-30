package cn.iocoder.yudao.module.ppd.controller.admin.screenpersonrealsituation.vo.nitoce;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeRespVO {
    private NoticeBase drNotice;
    private NoticeBase ppdNotice;
    private CtUpdateNotice ctUpdateNotice;
    private CtUpdateNotice drUpdateNotice;
    private PpdUpdateNotice ppdUpdateNotice;
}
