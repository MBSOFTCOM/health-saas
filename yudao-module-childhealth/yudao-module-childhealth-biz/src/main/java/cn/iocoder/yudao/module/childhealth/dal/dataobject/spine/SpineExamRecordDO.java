package cn.iocoder.yudao.module.childhealth.dal.dataobject.spine;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 脊柱骨骼筛查 DO
 */
@Data
@TableName("spine_exam_record")
public class SpineExamRecordDO {

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
     * 儿童ID
     */
    private Long childId;

    /**
     * 检查日期
     */
    private LocalDateTime examDate;

    /**
     * ATR角度（躯干旋转角度，度）- >=7° 阳性
     */
    private BigDecimal atrAngle;

    /**
     * 脊柱评估分级：NORMAL/MILD/MODERATE/SEVERE
     */
    private String spineAssessment;

    /**
     * Cobb角（度，X光测量）
     */
    private BigDecimal spineCurveAngle;

    /**
     * 双肩高度：LEVEL/EVEN/UNEVEN_LEFT_HIGH/UNEVEN_RIGHT_HIGH
     */
    private String shoulderLevel;

    /**
     * 骨盆倾斜：LEVEL/LEFT_TILT/RIGHT_TILT
     */
    private String pelvicTilt;

    /**
     * 肩胛骨突出：NONE/LEFT/RIGHT/BILATERAL
     */
    private String scapularProminence;

    /**
     * Adam前屈试验结果：NEGATIVE/POSITIVE
     */
    private String adamsTest;

    /**
     * 双下肢长度差（cm）
     */
    private BigDecimal legLengthDiff;

    /**
     * 足底压力检测（JSON或文本）
     */
    private String plantarPressure;

    /**
     * 足弓评估：NORMAL/FLAT_FOOT/HIGH_ARCH
     */
    private String archAssessment;

    /**
     * 左足弓指数
     */
    private BigDecimal leftArchIndex;

    /**
     * 右足弓指数
     */
    private BigDecimal rightArchIndex;

    /**
     * 姿势评估：NORMAL/FORWARD_HEAD/ROUNDED_SHOULDERS/PELVIC_TILT/OTHER
     */
    private String postureAssessment;

    /**
     * 姿势矫正训练建议
     */
    private String postureCorrectionPlan;

    /**
     * 风险分级：1正常 / 2预警 / 3异常
     */
    private Integer riskLevel;

    /**
     * 是否异常
     */
    private Boolean isAbnormal;

    /**
     * 诊断
     */
    private String diagnosis;

    /**
     * 处理建议
     */
    private String suggestion;

    /**
     * 检查医生
     */
    private Long doctorId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
