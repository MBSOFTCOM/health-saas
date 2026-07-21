package cn.iocoder.yudao.module.childhealth.dal.dataobject.workflow;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.*;

@Data
@TableName("follow_record")
public class FollowUpRecordDO {
    @TableId(type = IdType.AUTO) private Long id;
    private Long planId;
    private Long childId;
    private Long caseId;
    private LocalDate followDate;
    private Integer followType;
    private String followContent;
    private String healthStatus;
    private String measureData;
    private String guidance;
    private String nextPlan;
    private Long followDoctor;
    private LocalDateTime createTime;
}
