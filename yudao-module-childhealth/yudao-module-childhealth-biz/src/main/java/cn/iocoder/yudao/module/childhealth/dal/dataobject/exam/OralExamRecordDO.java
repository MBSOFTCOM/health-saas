package cn.iocoder.yudao.module.childhealth.dal.dataobject.exam;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 口腔检查 DO
 */
@Data
@TableName("oral_exam_record")
public class OralExamRecordDO {

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
     * 牙齿数量
     */
    private Integer toothCount;

    /**
     * 龋齿数
     */
    private Integer cariesCount;

    /**
     * 失牙数
     */
    private Integer missingTooth;

    /**
     * 补牙数
     */
    private Integer filledTooth;

    /**
     * dmft指数
     */
    private Integer dmftIndex;

    /**
     * 龋齿类型
     */
    private String cariesType;

    /**
     * 牙龈情况
     */
    private String gumCondition;

    /**
     * 口腔黏膜
     */
    private String oralMucosa;

    /**
     * 舌
     */
    private String tongue;

    /**
     * 腭
     */
    private String palate;

    /**
     * 咬合关系
     */
    private String occlusion;

    /**
     * 口腔卫生
     */
    private String oralHygiene;

    /**
     * 咬合异常
     */
    private String malocclusion;

    /**
     * 已做窝沟封闭牙数
     */
    private Integer sealantCount;

    /**
     * 窝沟封闭日期
     */
    private LocalDateTime sealantDate;

    /**
     * 最近涂氟日期
     */
    private LocalDateTime fluorideDate;

    /**
     * 涂氟次数（累计）
     */
    private Integer fluorideTimes;

    /**
     * 是否建议窝沟封闭
     */
    private Boolean needSealant;

    /**
     * 是否建议涂氟
     */
    private Boolean needFluoride;

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
     * 风险分级：1正常 / 2龋齿风险 / 3严重龋齿
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