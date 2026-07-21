package cn.iocoder.yudao.module.childhealth.dal.dataobject.rule;
import com.baomidou.mybatisplus.annotation.*; import lombok.Data; import java.time.LocalDateTime;
@Data @TableName("abnormal_detect_log") public class AbnormalDetectLogDO { @TableId(type=IdType.AUTO) private Long id; private Long examId; private Long ruleId; private Integer detectResult; private String abnormalValue; private Boolean isReminded; private Boolean isHandled; private LocalDateTime createTime; }
