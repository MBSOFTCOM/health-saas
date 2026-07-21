package cn.iocoder.yudao.module.childhealth.dal.dataobject.workflow;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.*;

@Data
@TableName("exam_record")
public class HealthCheckupDO {
    @TableId(type = IdType.AUTO) private Long id;
    private String examNo;
    private Long childId;
    @TableField("exam_date") private LocalDate checkupDate;
    @TableField("month_age") private Integer ageMonths;
    @TableField("exam_type") private String checkupType;
    @TableField(exist = false) private BigDecimal height;
    @TableField(exist = false) private BigDecimal weight;
    @TableField(exist = false) private String nutritionStatus;
    @TableField("has_abnormal") private Boolean isAbnormal;
    @TableField("abnormal_tags") private String abnormalItems;
    private Long doctorId;
    @TableField("check_status") private Integer reviewStatus;
    @TableField(exist = false) private LocalDateTime reviewedAt;
    @TableField(exist = false) private Long reviewedBy;
    @TableField("create_time") private LocalDateTime createdAt;
    @TableField("update_time") private LocalDateTime updatedAt;
}
