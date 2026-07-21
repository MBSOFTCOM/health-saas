package cn.iocoder.yudao.module.childhealth.dal.dataobject.exam;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 眼保健检查 DO
 */
@Data
@TableName("eye_exam_record")
public class EyeExamRecordDO {

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
     * 眼睑
     */
    private String eyelid;

    /**
     * 结膜
     */
    private String conjunctiva;

    /**
     * 角膜
     */
    private String cornea;

    /**
     * 瞳孔
     */
    private String pupil;

    /**
     * 左眼视力
     */
    private String leftVision;

    /**
     * 右眼视力
     */
    private String rightVision;

    /**
     * 左眼屈光度
     */
    private String leftDiopter;

    /**
     * 右眼屈光度
     */
    private String rightDiopter;

    /**
     * 左眼散光
     */
    private String leftAstigmatism;

    /**
     * 右眼散光
     */
    private String rightAstigmatism;

    /**
     * 左眼矫正视力
     */
    private String leftCorrectedVision;

    /**
     * 右眼矫正视力
     */
    private String rightCorrectedVision;

    /**
     * 左眼眼轴长度（mm）- 用于远视储备评估
     */
    private java.math.BigDecimal leftAxialLength;

    /**
     * 右眼眼轴长度（mm）- 用于远视储备评估
     */
    private java.math.BigDecimal rightAxialLength;

    /**
     * 左眼角膜曲率
     */
    private String leftKeratometry;

    /**
     * 右眼角膜曲率
     */
    private String rightKeratometry;

    /**
     * 左眼远视储备（D）- 屈光储备评估核心指标
     */
    private java.math.BigDecimal leftHyperopiaReserve;

    /**
     * 右眼远视储备（D）- 屈光储备评估核心指标
     */
    private java.math.BigDecimal rightHyperopiaReserve;

    /**
     * 眼位
     */
    private String eyePosition;

    /**
     * 斜视
     */
    private String strabismus;

    /**
     * 红球试验
     */
    private String redReflexTest;

    /**
     * 瞳孔对光反射
     */
    private String pupilLightReflex;

    /**
     * 外眼检查
     */
    private String eyeAppearance;

    /**
     * 眼底检查
     */
    private String fundusExam;

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
     * 风险分级：1正常 / 2预警 / 3异常
     */
    private Integer riskLevel;

    /**
     * 是否异常
     */
    private Boolean isAbnormal;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}