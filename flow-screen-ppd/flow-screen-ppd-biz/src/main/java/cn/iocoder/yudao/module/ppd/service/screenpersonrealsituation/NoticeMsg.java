package cn.iocoder.yudao.module.ppd.service.screenpersonrealsituation;

public enum NoticeMsg {
//    定义枚举类，ppd通知，DR通知，CT通知，使用值为字符串

    PPD_UPDATE_NOTICE("学生PPD筛查通知:PPD筛查结果已更新"),
    DR_UPDATE_NOTICE("学生DR筛查通知:DR胸片筛查结果已更新"),
    CT_UPDATE_NOTICE("学生CT筛查通知:CT胸片筛查结果已更新");
    final String value;

    NoticeMsg(String s) {
        this.value=s;
    }

    public String getValue() {
        return value;
    }
}
