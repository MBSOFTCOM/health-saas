package cn.iocoder.yudao.module.ppd.dal.dataobject.screensum;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 汇总 DO
 *
 * @author 芋道源码
 */
@TableName("tb_screen_sum")
@KeySequence("tb_screen_sum_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScreenSumDO {
    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 筛查年份
     */
    private String year;
    /**
     * 对应摸底表中id
     */
    private Long personId;
    /**
     * 身份证号
     */
    private String idNum;
    /**
     * 同步时唯一编码
     */
    private Long syncId;

    /**
     * 采集表id
     */
    private Long collectId;
    /**
     * ppd表id
     */
    private Long ppdId;
    /**
     * 筛查类型
     */
    private Integer screenType;
    /**
     * 筛查次序
     */
    private Integer screenOrder;
    /**
     * 当前已完成的分组
     */
    private String curFinish;
    /**
     * dr胸片表id
     */
    private Long chestRadiographId;
    /**
     * ct表id
     */
    private Long computedTomographyId;


}