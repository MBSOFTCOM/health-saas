package cn.iocoder.yudao.module.childhealth.dal.dataobject.workflow;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import java.math.BigDecimal;
import java.time.*;

/**
 * 儿童基本信息数据对象 DO
 *
 * @author 系统
 */
@Data
@TableName("child_base_info")
public class ChildInfoDO {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 儿童唯一编码
     */
    private String childCode;

    /**
     * 姓名
     */
    @TableField("name")
    private String childName;

    /**
     * 性别 1男 2女
     */
    private Integer gender;

    /**
     * 出生日期
     */
    private LocalDate birthDate;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 出生体重(kg)
     */
    private BigDecimal birthWeight;

    /**
     * 出生身长(cm)
     */
    private BigDecimal birthHeight;

    /**
     * 胎龄(周)
     */
    private Integer gestationalAge;

    /**
     * 是否早产 0否 1是
     */
    private Integer isPremature;

    /**
     * 是否高危儿
     */
    private Integer isHighRisk;

    /**
     * 高危标签
     */
    @TableField("high_risk_tags")
    private String highRiskTags;

    /**
     * 二维码URL
     */
    private String qrCode;

    /**
     * 首次就诊日期
     */
    private LocalDate firstVisitDate;

    /**
     * 建档来源 1手动 2微信自助 3孕保拉取 4HIS自动
     */
    private Integer registerSource;

    /**
     * 高危管理等级 1一般 2重点关注 3严密管理
     */
    private Integer highRiskLevel;

    /**
     * 状态 1正常 2转出 3死亡
     */
    private Integer status;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;

    // ========== 非数据库字段 ==========

    /**
     * 监护人姓名（用于查询和展示）
     */
    @TableField(exist = false)
    private String parentName;

    /**
     * 监护人电话（用于查询和展示）
     */
    @TableField(exist = false)
    private String parentPhone;

    /**
     * 创建人（用于权限控制）
     */
    @TableField(exist = false)
    private Long createdBy;
}
