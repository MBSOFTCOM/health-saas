package cn.iocoder.yudao.module.childhealth.dal.dataobject.scale;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 心理/发育量表表 - 支持15+种量表
 * 
 * 支持的量表：
 * 1. Peabody 运动发育量表 - 评估大运动、精细运动
 * 2. Gesell 发育诊断 - 综合评估
 * 3. DDST 丹佛筛查 - 0-6岁发育筛查
 * 4. Bayley 婴幼儿发育量表
 * 5. SNAP-IV ADHD评估
 * 6. Conners 注意力评估
 * 7. GAD-7 焦虑评估
 * 8. PHQ-9 抑郁评估
 * 9. MHT 心理健康测试
 * 10. SDQ 优势困难问卷
 * 11. 韦氏智力量表 (儿童版)
 * 12. 瑞文渐进矩阵测试
 * 13. 孤独症谱系障碍诊断 (DSM-5 ASD)
 * 14. 感觉统合评定量表
 * 15. 社会生活能力评估
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("scale_config")
public class PsychologicalScale {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 量表编码 (如PEABODY_MOTOR, GESELL_GENERAL等)
     */
    private String scaleCode;

    /**
     * 量表名称
     */
    private String scaleName;

    /**
     * 量表英文名
     */
    @TableField(exist = false)
    private String scaleNameEn;

    /**
     * 量表类型 (MOTOR_DEVELOPMENT, COGNITIVE, BEHAVIORAL, PSYCHOLOGICAL, AUTISM)
     */
    private String scaleType;

    /**
     * 应用年龄范围 - 最小(月)
     */
    private Integer applicableAgeMin;

    /**
     * 应用年龄范围 - 最大(月)
     */
    private Integer applicableAgeMax;

    /**
     * 填写人 (PARENT, TEACHER, SELF, CLINICIAN)
     */
    @TableField(exist = false)
    private String respondentType;

    /**
     * 题目总数
     */
    @TableField(exist = false)
    private Integer itemCount;

    /**
     * 题目详情 (JSON)
     * {
     *   "items": [
     *     {
     *       "itemCode": "Q1",
     *       "itemText": "问题描述",
     *       "options": [
     *         {"value": 0, "label": "完全不符"},
     *         {"value": 1, "label": "有时符合"},
     *         {"value": 2, "label": "基本符合"},
     *         {"value": 3, "label": "完全符合"}
     *       ],
     *       "scoring": {"type": "direct", "weight": 1}
     *     }
     *   ]
     * }
     */
    @TableField(exist = false)
    private String items;

    /**
     * 计分规则 (JSON)
     * {
     *   "scoringMethod": "sum|mean|weighted",
     *   "totalScoreRange": {"min": 0, "max": 100},
     *   "subscales": [
     *     {
     *       "name": "大运动",
     *       "itemCodes": ["Q1", "Q2", "Q3"],
     *       "scoring": {"method": "sum"}
     *     }
     *   ]
     * }
     */
    @TableField("scoring_rule")
    private String scoringRules;

    /**
     * 总分范围 (如"0-40")
     */
    @TableField(exist = false)
    private String totalScoreRange;

    /**
     * 诊断分界值 (风险分级标准)
     * {
     *   "noRisk": {"min": 0, "max": 10, "label": "无风险"},
     *   "lowRisk": {"min": 11, "max": 20, "label": "低风险"},
     *   "highRisk": {"min": 21, "max": 40, "label": "高风险"}
     * }
     */
    @TableField(exist = false)
    private String diagnosticCriteria;

    /**
     * 风险分级标准 (JSON)
     */
    @TableField("risk_level_rule")
    private String riskLevels;

    /**
     * 诊断标准说明
     */
    @TableField(exist = false)
    private String diagnosticExplanation;

    /**
     * 量表描述
     */
    private String description;

    /**
     * 量表版本
     */
    @TableField(exist = false)
    private String version;

    /**
     * 发布年份
     */
    @TableField(exist = false)
    private Integer publishYear;

    /**
     * 参考文献
     */
    @TableField(exist = false)
    private String references;

    /**
     * 状态 (ACTIVE, INACTIVE, DEPRECATED)
     */
    private String status;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField(exist = false)
    private LocalDateTime updatedAt;

    /**
     * 是否删除
     */
    @TableField(exist = false)
    private Boolean deleted;
}
