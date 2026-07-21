package cn.iocoder.yudao.module.childhealth.dal.dataobject.workflow;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.*;

@Data
@TableName("screening_batch")
public class HealthScreeningBatchDO {
    @TableId(type = IdType.AUTO) private Long id;
    private String batchNo;
    private String batchName;
    private Long yearId;
    private Long schoolId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer targetCount;
    private Integer actualCount;
    private Integer batchStatus;
    @TableField("create_time") private LocalDateTime createdAt;
}
