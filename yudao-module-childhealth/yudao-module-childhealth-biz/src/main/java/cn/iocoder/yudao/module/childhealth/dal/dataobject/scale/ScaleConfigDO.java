package cn.iocoder.yudao.module.childhealth.dal.dataobject.scale;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 量表配置DO
 */
@Data
@TableName("scale_config")
public class ScaleConfigDO {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String scaleCode;
    private String scaleName;
    private String scaleType;
    private Integer applicableAgeMin;
    private Integer applicableAgeMax;
    private Integer totalScore;
    private String scoringRule;
    private String riskLevelRule;
    private BigDecimal abnormalThreshold;
    private String description;
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}