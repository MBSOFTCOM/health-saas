package cn.iocoder.yudao.module.ppd.controller.admin.screenpersonrealsituation.vo;


import cn.iocoder.yudao.module.ppd.dal.dataobject.screenpersonrealsituation.ScreenPersonDO;
import lombok.Data;

@Data
public class ImportVO {
    private String code;
    private String name;
    private String screenId;
    private ScreenPersonDO screenPersonDO;
}
