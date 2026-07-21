package cn.iocoder.yudao.module.childhealth.dal.dataobject.childbase;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("high_risk_factor_config")
public class HighRiskFactorConfigDO {
    @TableId(type = IdType.AUTO) private Long id;
    private String factorCode;
    private String factorName;
    private String category;
    private Integer riskLevel;
    private String conditionLogic;
    private Boolean isAutoDetect;
    private Integer sortOrder;
    private Integer status;
    private LocalDateTime createTime;
}
