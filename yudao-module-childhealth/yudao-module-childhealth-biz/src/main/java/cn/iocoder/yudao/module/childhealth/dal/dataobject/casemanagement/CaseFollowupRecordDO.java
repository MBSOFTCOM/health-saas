package cn.iocoder.yudao.module.childhealth.dal.dataobject.casemanagement;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDate;

/**
 * 专案随访记录表 DO
 *
 * 对应表: case_followup_record
 * 模块: B.专案管理
 * 创建日期: 2026-07-20
 */
@TableName("case_followup_record")
@KeySequence("case_followup_record_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CaseFollowupRecordDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 专案ID
     */
    private Long caseId;

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
     * 随访类型 1面诊 2电话 3短信 4微信 5线上问卷
     */
    private Integer followupType;

    /**
     * 随访内容
     */
    private String followupContent;

    /**
     * 体征数据JSON（体重/身高/BMI等）
     */
    private String bodyDataJson;

    /**
     * 健康变化 1改善 2稳定 3恶化
     */
    private Integer healthChange;

    /**
     * 干预意见
     */
    private String intervention;

    /**
     * 用药情况
     */
    private String medication;

    /**
     * 下次随访日期
     */
    private LocalDate nextFollowupDate;

    /**
     * 随访医生ID
     */
    private Long doctorId;

    /**
     * 医生姓名
     */
    private String doctorName;

    /**
     * 附件URL JSON
     */
    private String attachmentUrls;

}
