package cn.iocoder.yudao.module.childhealth.dal.dataobject.childbase;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("delivery_info")
public class DeliveryInfoDO {
    @TableId(type = IdType.AUTO) private Long id;
    private Long childId;
    private Integer deliveryType;
    private Integer gestationalWeeks;
    private Integer gestationalDays;
    private BigDecimal birthWeight;
    private BigDecimal birthLength;
    private Integer apgar1min;
    private Integer apgar5min;
    private Integer apgar10min;
    private String birthHospital;
    private String birthDoctor;
    private Integer motherAge;
    private String pregnancyComplication;
    private String deliveryComplication;
    private String neonatalDiagnosis;
    private Boolean isPremature;
    private Boolean isLowWeight;
    private Boolean isMultiple;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
