package cn.iocoder.yudao.module.childhealth.dal.dataobject.exam;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 听力检查 DO
 */
@Data
@TableName("hearing_exam_record")
public class HearingExamRecordDO {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 体检记录ID
     */
    private Long examId;

    /**
     * 左耳廓
     */
    private String auricleLeft;

    /**
     * 右耳廓
     */
    private String auricleRight;

    /**
     * 左外耳道
     */
    private String earCanalLeft;

    /**
     * 右外耳道
     */
    private String earCanalRight;

    /**
     * 左鼓膜
     */
    private String tympanicLeft;

    /**
     * 右鼓膜
     */
    private String tympanicRight;

    /**
     * 筛查方法 OAE/AABR
     */
    private String screeningMethod;

    /**
     * 左耳筛查结果 PASS/REFER
     */
    private String screeningLeft;

    /**
     * 右耳筛查结果 PASS/REFER
     */
    private String screeningRight;

    /**
     * 左耳检查结果
     */
    private String leftEarResult;

    /**
     * 右耳检查结果
     */
    private String rightEarResult;

    /**
     * 听力筛查 通过/未通过
     */
    private String hearingScreening;

    /**
     * 耳廓外观
     */
    private String earAppearance;

    /**
     * 外耳道
     */
    private String externalAuditory;

    /**
     * 鼓膜
     */
    private String tympanicMembrane;

    /**
     * 诊断
     */
    private String diagnosis;

    /**
     * 异常项目
     */
    private String abnormalItems;

    /**
     * 处理建议
     */
    private String suggestion;

    /**
     * 模板代码
     */
    private String templateCode;

    /**
     * 是否异常
     */
    private Boolean isAbnormal;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}