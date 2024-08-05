package cn.iocoder.yudao.module.ppd.dal.dataobject.userscreenpoint;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 用户筛查点角色 DO
 *
 * @author 芋道源码
 */
@TableName("tb_user_screen_point")
@KeySequence("tb_user_screen_point_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserScreenPointDO {

    /**
     * 主键id
     */
    @TableId
    private Long id;
    /**
     * 用户角色id
     */
    private Long userRoleId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 角色id
     */
    private Long roleId;
    /**
     * 筛查点id
     */
    private Long screenPointId;
    /**
     * 是否删除
     */
    @TableLogic
    private Boolean deleted;

}