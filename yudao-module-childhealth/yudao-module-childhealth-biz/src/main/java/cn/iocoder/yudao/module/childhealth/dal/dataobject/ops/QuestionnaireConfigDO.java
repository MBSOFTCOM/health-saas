package cn.iocoder.yudao.module.childhealth.dal.dataobject.ops;
import com.baomidou.mybatisplus.annotation.*; import lombok.Data; import java.time.LocalDateTime;
@Data @TableName("questionnaire_config") public class QuestionnaireConfigDO { @TableId(type=IdType.AUTO) private Long id; private String questionnaireCode; private String questionnaireName; private String questionnaireType; private String applicableScene; private String questions; private String scoringRule; private Integer status; private LocalDateTime createTime; }
