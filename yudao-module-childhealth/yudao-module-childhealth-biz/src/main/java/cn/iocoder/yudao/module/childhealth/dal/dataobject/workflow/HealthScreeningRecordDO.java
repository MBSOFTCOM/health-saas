package cn.iocoder.yudao.module.childhealth.dal.dataobject.workflow;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.*;

@Data
@TableName("screening_record")
public class HealthScreeningRecordDO {
    @TableId(type = IdType.AUTO) private Long id;
    private String recordNo;
    private Long batchId;
    private Long studentId;
    private LocalDate screeningDate;
    private Integer checkStatus;
    private Boolean hasPositive;
    private String positiveItems;
    private Long auditDoctor;
    private LocalDateTime auditTime;
    @TableField("create_time") private LocalDateTime createdAt;
    @TableField("update_time") private LocalDateTime updatedAt;
}
