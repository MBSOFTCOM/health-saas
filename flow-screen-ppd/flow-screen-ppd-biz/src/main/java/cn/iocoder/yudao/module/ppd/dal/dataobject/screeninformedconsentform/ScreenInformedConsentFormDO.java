package cn.iocoder.yudao.module.ppd.dal.dataobject.screeninformedconsentform;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 知情同意书 DO
 *
 * @author 福乐云
 */
@TableName("tb_screen_informed_consent_form")
@KeySequence("tb_screen_informed_consent_form_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScreenInformedConsentFormDO extends BaseDO {

    /**
     * 自增id
     */
    @TableId
    private Long id;
    /**
     * 受筛查学生的id（待筛查人员id）
     */
    private Long studentId;
    /**
     * 学校
     */
    private String schoolName;
    /**
     * 班级
     */
    private String classroom;
    /**
     * 是否签署1：是  2：否
     */
    private Integer isSign;
    /**
     * 拒绝签署原因
     */
    private String reason;
    /**
     * 家长签名图片地址
     */
    private String signature;

}