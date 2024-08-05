package cn.iocoder.yudao.module.ppd.dal.dataobject.screenpoint;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 筛查点 DO
 *
 * @author 芋道源码
 */
@TableName("tb_screen_point")
@KeySequence("tb_screen_point_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScreenPointDO extends BaseDO {

    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 筛查点名称
     */
    @ExcelProperty("筛查点名称")
    private String name;

    /**
     * 工作人员
     */
    private String worker;

    /**
     * 采集组工作人员
     */
    private String collectWorker;
    /**
     * PDD组工作人员
     */
    private String ppdWorker;
    /**
     * DR/CT组工组人员
     */
    private String drctWorker;
    /**
     * 痰检组工作人员
     */
    private String sputumWorker;
    /**
     * 实验组工作人员
     */
    private String experimentWorker;
    /**
     * 心电图组工作人员
     */
    private String electrocardiogramWorker;
    /**
     * 诊断组工作人员
     */
    private String diagnosisWorker;

    /**
     * 筛查单位
     */
    private String screenDept;

    /**
     * 工作年度
     */
    private Integer year;

}