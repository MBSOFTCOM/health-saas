package cn.iocoder.yudao.module.childhealth.dal.dataobject.ops;
import com.baomidou.mybatisplus.annotation.*; import lombok.Data; import java.time.LocalDateTime;
@Data @TableName("reminder_rule_config") public class ReminderRuleDO { @TableId(type=IdType.AUTO) private Long id; private String ruleCode; private String ruleName; private Integer examType; private String targetAge; private Integer remindAdvanceDays; private String remindFrequency; private String messageTemplate; private Integer status; private LocalDateTime createTime; }
