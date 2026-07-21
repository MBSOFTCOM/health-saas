package cn.iocoder.yudao.module.childhealth.dal.dataobject.scale;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 儿童发育评估 DO
 */
@Data
@TableName("child_development_assessment")
public class ChildDevelopmentAssessmentDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 儿童ID */
    private Long childId;

    /** 关联体检ID */
    private Long examId;

    /** 量表代码 DDST/GESELL/WISC */
    private String scaleCode;

    /** 量表名称 */
    private String scaleName;

    /** 评估日期 */
    private LocalDate assessmentDate;

    /** 评估月龄 */
    private Integer monthAge;

    /** 运动发育得分 */
    private BigDecimal motorScore;

    /** 语言发育得分 */
    private BigDecimal languageScore;

    /** 认知发育得分 */
    private BigDecimal cognitiveScore;

    /** 社交发育得分 */
    private BigDecimal socialScore;

    /** 原始评分JSON */
    private String rawScore;

    /** 标准分JSON */
    private String standardScore;

    /** 结果摘要 */
    private String resultSummary;

    /** 整体发育水平 */
    private String overallLevel;

    /** 是否发育迟缓 */
    private Boolean isDelayed;

    /** 迟缓领域JSON */
    private String delayedDomains;

    /** 评估者 */
    private String assessor;

    /** 指导建议 */
    private String suggestion;

    /** 报告路径 */
    private String reportUrl;

    /** 医生ID */
    private Long doctorId;

    /** 创建时间 */
    private LocalDateTime createTime;
}
