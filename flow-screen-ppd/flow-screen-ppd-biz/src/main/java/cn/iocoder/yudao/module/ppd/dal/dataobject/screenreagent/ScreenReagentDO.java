package cn.iocoder.yudao.module.ppd.dal.dataobject.screenreagent;

import lombok.*;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 试剂 DO
 *
 * @author 侯卿
 */
@TableName("tb_screen_reagent")
@KeySequence("tb_screen_reagent_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScreenReagentDO extends BaseDO {

    /**
     * 试剂名称
     */
    private String name;
    /**
     * 试剂类型
     */
    private Integer type;
    /**
     * 转换系数（人次）
     */
    private Integer reagentSpecsNum;
    /**
     * 是否启用
     */
    private Integer usable;
    /**
     * 效价
     */
    private BigDecimal titer;
    /**
     * 效价单位
     */
    private Integer potencyUnit;
    /**
     * 规格
     */
    private BigDecimal specification;
    /**
     * 规格单位
     */
    private Integer specificationUnit;
    /**
     * 包装单位
     */
    private Integer packageUnit;
    /**
     * 供应商
     */
    private String manufacturer;
    /**
     * 库存预警值（按试剂）
     */
    private Integer threshold;
    /**
     * 自增主键id
     */
    @TableId
    private Long id;

}