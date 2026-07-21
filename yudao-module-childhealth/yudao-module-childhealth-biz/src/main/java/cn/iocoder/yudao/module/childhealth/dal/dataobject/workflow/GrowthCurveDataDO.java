package cn.iocoder.yudao.module.childhealth.dal.dataobject.workflow;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("growth_curve_data")
public class GrowthCurveDataDO {
    @TableId(type = IdType.AUTO) private Long id;
    private Long childId;
    private LocalDate measureDate;
    private Integer monthAge;
    private BigDecimal height;
    private BigDecimal weight;
    private BigDecimal headCircumference;
    private BigDecimal bmi;
    private Integer dataSource;
    private LocalDateTime createTime;
}
