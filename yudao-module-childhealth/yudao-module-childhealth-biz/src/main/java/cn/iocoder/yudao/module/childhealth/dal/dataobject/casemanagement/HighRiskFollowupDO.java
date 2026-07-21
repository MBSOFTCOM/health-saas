package cn.iocoder.yudao.module.childhealth.dal.dataobject.casemanagement;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 高危儿随访表 DO
 *
 * 对应表: high_risk_followup
 * 模块: B.高危儿专案管理
 * 创建日期: 2026-07-20
 */
@TableName("high_risk_followup")
@KeySequence("high_risk_followup_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HighRiskFollowupDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 高危新生儿ID
     */
    private Long newbornId;

    /**
     * 儿童ID
     */
    private Long childId;

    /**
     * 随访编号
     */
    private String followupNo;

    /**
     * 随访日期
     */
    private LocalDate followupDate;

    /**
     * 随访类型 1院内 2家庭 3电话
     */
    private Integer followupType;

    /**
     * 月龄
     */
    private Integer ageMonth;

    /**
     * 体重(kg)
     */
    private BigDecimal bodyWeight;

    /**
     * 身长(cm)
     */
    private BigDecimal bodyLength;

    /**
     * 头围(cm)
     */
    private BigDecimal headCircumference;

    /**
     * 体征数据JSON
     */
    private String bodyDataJson;

    /**
     * 发育状态
     */
    private String developmentStatus;

    /**
     * 喂养情况
     */
    private String feedingStatus;

    /**
     * 营养状况
     */
    private String nutritionStatus;

    /**
     * 医生ID
     */
    private Long doctorId;

    /**
     * 医生姓名
     */
    private String doctorName;

    /**
     * 下次随访日期
     */
    private LocalDate nextFollowupDate;

    /**
     * 下次随访月龄
     */
    private Integer nextFollowupAgeMonth;

    /**
     * 备注
     */
    private String remark;

}
