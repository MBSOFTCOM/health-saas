package cn.iocoder.yudao.module.cd.enums;

public enum TableName {
    COLLECT("tb_screen_collect"),
    PPD("tb_screen_ppd"),
    CHEST_RADIOGRAPH("tb_screen_chest_radiograph"),
    SUM("tb_screen_sum"),
    IMAGES("tb_screen_images"),
    PERSON("tb_screen_person"),
    ;
    private final String tableName;

    TableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }
}
