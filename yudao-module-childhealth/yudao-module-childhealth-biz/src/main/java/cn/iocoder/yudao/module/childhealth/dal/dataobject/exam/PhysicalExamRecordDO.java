package cn.iocoder.yudao.module.childhealth.dal.dataobject.exam;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 体格检查记录 DO
 */
@Data
@TableName("physical_exam_record")
public class PhysicalExamRecordDO {

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
     * 身高cm
     */
    private BigDecimal height;

    /**
     * 体重kg
     */
    private BigDecimal weight;

    /**
     * 头围cm
     */
    private BigDecimal headCircumference;

    /**
     * 胸围cm
     */
    private BigDecimal chestCircumference;

    /**
     * BMI
     */
    private BigDecimal bmi;

    /**
     * 身高SD值
     */
    private BigDecimal heightSd;

    /**
     * 体重SD值
     */
    private BigDecimal weightSd;

    /**
     * 头围SD值
     */
    private BigDecimal headSd;

    /**
     * 体温℃
     */
    private BigDecimal bodyTemp;

    /**
     * 心率次/分
     */
    private Integer heartRate;

    /**
     * 呼吸频率次/分
     */
    private Integer respiratoryRate;

    /**
     * 皮肤
     */
    private String skinCondition;

    /**
     * 淋巴结
     */
    private String lymphNode;

    /**
     * 头部形态
     */
    private String headShape;

    /**
     * 囟门
     */
    private String fontanelle;

    /**
     * 胸部
     */
    private String chestExam;

    /**
     * 心脏
     */
    private String heartExam;

    /**
     * 肺部
     */
    private String lungExam;

    /**
     * 腹部
     */
    private String abdomenExam;

    /**
     * 脊柱（旧字段：正常/侧弯/后凸/前凸枚举）
     */
    private String spineExam;

    /**
     * ATR 角度（躯干旋转角度，度）- 脊柱侧弯筛查核心指标，>=7° 阳性
     */
    private java.math.BigDecimal atrAngle;

    /**
     * 脊柱评估分级：NORMAL正常 / MILD轻度侧弯 / MODERATE中度侧弯 / SEVERE重度侧弯
     */
    private String spineAssessment;

    /**
     * 足底压力检测结果（左/右，JSON 或文本描述）
     */
    private String plantarPressure;

    /**
     * 足弓评估：NORMAL正常 / FLAT_FOOT扁平足 / HIGH_ARCH高弓足
     */
    private String archAssessment;

    /**
     * 姿势评估：NORMAL正常 / ABNORMAL异常
     */
    private String postureAssessment;

    /**
     * 姿势矫正训练建议
     */
    private String postureCorrectionPlan;

    /**
     * 四肢
     */
    private String limbsExam;

    /**
     * 外生殖器
     */
    private String genitalia;

    /**
     * 神经系统
     */
    private String nervousSystem;

    /**
     * 生长评估结果
     */
    private String growthAssessment;

    /**
     * 是否异常
     */
    private Boolean isAbnormal;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}