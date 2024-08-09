package cn.iocoder.yudao.module.ppd.dal.dataobject.screenconsumerecord;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 消耗管理记录 DO
 *
 * @author 侯卿
 */
@TableName("tb_screen_consume_record")
@KeySequence("tb_screen_consume_record_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScreenConsumeRecordDO extends BaseDO {

    /**
     * 主键id
     */
    @TableId
    private Long id;
    /**
     * 变化量
     */
    private Integer changeNumber;
    /**
     * 变化类型（1：筛查自动扣减，2：手动增加库存，3：手动减少库存）
     */
    private Integer type;
    /**
     * 消耗管理表id
     */
    private Long consumeId;

}