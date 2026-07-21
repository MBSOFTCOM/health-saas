package cn.iocoder.yudao.module.childhealth.dal.dataobject.scale;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.*;
@Data @TableName("scale_assessment_record")
public class ScaleAssessmentRecordDO {
    @TableId(type = IdType.AUTO) private Long id; private String recordNo; private Long childId; private Long scaleId;
    private LocalDate assessmentDate; private Integer assessorType; private Long assessorId; private String answers;
    private BigDecimal totalScore; private String dimensionScores; private Integer riskLevel; private Boolean isAbnormal;
    private String assessmentConclusion; private String suggestion; private LocalDateTime createTime;
}
