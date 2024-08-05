package cn.iocoder.yudao.module.ppd.controller.admin.synchronization.vo;

import lombok.Data;

@Data
public class TableSchema {

    /**
     * 表名
     */
    private String tableName;

    /**
     * 表注释
     */
    private String tableComment;
}
