package cn.iocoder.yudao.module.childhealth.dal.dataobject.workflow;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("case_recovery_assessment")
public class CaseRecoveryAssessmentDO {
    @TableId(type = IdType.AUTO) private Long id;
    private Long caseId;
    private LocalDate assessmentDate;
    private String assessmentContent;
    private Integer recoveryLevel;
    private Boolean isQualified;
    private Long doctorId;
    private LocalDateTime createTime;
}
