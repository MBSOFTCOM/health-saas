package cn.iocoder.yudao.module.childhealth.dal.dataobject.rule;
import com.baomidou.mybatisplus.annotation.*; import lombok.Data; import java.time.LocalDateTime;
@Data @TableName("abnormal_rule") public class AbnormalRuleDO { @TableId(type=IdType.AUTO) private Long id; private String ruleCode; private String ruleName; private String checkItem; private String conditionLogic; private Integer abnormalLevel; private Boolean autoCreateCase; private String caseType; private Integer status; private LocalDateTime createTime; }
