package cn.iocoder.yudao.module.childhealth.dal.dataobject.workflow;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.*;

@Data
@TableName("case_registration")
public class CaseRegistrationDO {
    @TableId(type = IdType.AUTO) private Long id;
    private String caseNo;
    private Long childId;
    private Long caseTypeId;
    private LocalDate registrationDate;
    private Integer caseSource;
    private String initialDiagnosis;
    private Integer caseLevel;
    private Long responsibleDoctor;
    private Integer caseStatus;
    private LocalDate dischargeDate;
    private String dischargeReason;
    @TableField("create_time") private LocalDateTime createdAt;
    @TableField("update_time") private LocalDateTime updatedAt;
}
