package cn.iocoder.yudao.module.ppd.dal.dataobject.screendistrict;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 区划 DO
 *
 * @author 侯卿
 */
@TableName("tb_screen_district")
@KeySequence("tb_screen_district_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScreenDistrictDO extends BaseDO {

    /**
     * 自增类型（主键）
     */
    @TableId
    private Long id;
    /**
     * 区划代码（唯一）
     */
    private String code;
    /**
     * 区划级别
     */
    private String level;
    /**
     * 区划名称
     */
    private String name;
    /**
     * 上级地区code
     */
    private String parentCode;

}