package cn.iocoder.yudao.module.childhealth.dal.dataobject.ops;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("questionnaire_answer")
public class QuestionnaireAnswerDO {
    @TableId(type = IdType.AUTO) private Long id;
    private Long questionnaireId;
    private Long childId;
    private Integer fillType;
    private Long fillerId;
    private String answers;
    private LocalDateTime fillTime;
    private Long associatedRecord;
    private LocalDateTime createTime;
}
