package cn.iocoder.yudao.module.ppd.dal.dataobject.screenconsume;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 消耗管理 DO
 *
 * @author 侯卿
 */
@TableName("tb_screen_consume")
@KeySequence("tb_screen_consume_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScreenConsumeDO extends BaseDO {

    /**
     * 主键id
     */
    @TableId
    private Long id;
    /**
     * 试剂id
     */
    private Long reagentId;
    /**
     * 试剂名称
     */
    private String reagentName;
    /**
     * 试剂类型
     */
    private Integer reagentType;
    /**
     * 消耗序位
     */
    private Integer consumeOrder;
    /**
     * 批次号
     */
    private String bathNumber;
    /**
     * 入库量（按试剂）
     */
    private Integer inboundNumber;
    /**
     * 当前库存
     */
    private Integer currentNumber;
    /**
     * 生产日期
     */
    private LocalDateTime manufactureDate;
    /**
     * 转换系数（人次）
     */
    private Integer reagentSpecsNum;
    /**
     * 库存预警值（按试剂）
     */
    private Integer threshold;
    /**
     * 有效期
     */
    private String indate;
    /**
     * 是否启用
     */
    private Integer usable;
    /**
     * 所属部门id
     */
    private Long deptId;

}