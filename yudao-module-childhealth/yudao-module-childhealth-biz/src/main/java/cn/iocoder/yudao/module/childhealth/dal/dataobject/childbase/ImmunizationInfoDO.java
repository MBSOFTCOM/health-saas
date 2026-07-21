package cn.iocoder.yudao.module.childhealth.dal.dataobject.childbase;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("immunization_info")
public class ImmunizationInfoDO {
    @TableId(type = IdType.AUTO) private Long id;
    private Long childId;
    private String vaccineName;
    private String vaccineCode;
    private LocalDate inoculateDate;
    private Integer doseNo;
    private String inoculateOrg;
    private String batchNo;
    private String manufacturer;
    private Boolean isReplan;
    private String replanReason;
    private LocalDateTime createTime;
}
