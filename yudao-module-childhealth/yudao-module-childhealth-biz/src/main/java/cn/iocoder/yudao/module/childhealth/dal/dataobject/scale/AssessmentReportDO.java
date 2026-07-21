package cn.iocoder.yudao.module.childhealth.dal.dataobject.scale;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.*;
@Data @TableName("assessment_report")
public class AssessmentReportDO {
    @TableId(type = IdType.AUTO) private Long id; private String reportNo; private Long childId; private Long assessmentId;
    private String reportType; private LocalDate reportDate; private String reportContent; private String reportUrl; private LocalDateTime createTime;
}
