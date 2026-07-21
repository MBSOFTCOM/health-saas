package cn.iocoder.yudao.module.childhealth.dal.dataobject.workflow;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 监护人信息数据对象 DO
 *
 * @author 系统
 */
@Data
@TableName("guardian_info")
public class GuardianInfoDO {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 儿童ID
     */
    private Long childId;

    /**
     * 关系 1父亲 2母亲 3其他
     */
    private Integer relation;

    /**
     * 监护人姓名
     */
    @TableField("name")
    private String guardianName;

    /**
     * 监护人电话
     */
    @TableField("mobile")
    private String guardianPhone;

    /**
     * 监护人身份证号
     */
    private String idCard;

    /**
     * 是否主要监护人
     */
    private Integer isPrimary;

    /**
     * 微信OpenID
     */
    private String wechatOpenid;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
