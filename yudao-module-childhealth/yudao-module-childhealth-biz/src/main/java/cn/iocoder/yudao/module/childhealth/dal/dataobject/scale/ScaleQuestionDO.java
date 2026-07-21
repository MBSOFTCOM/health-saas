package cn.iocoder.yudao.module.childhealth.dal.dataobject.scale;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;
@Data @TableName("scale_question")
public class ScaleQuestionDO {
    @TableId(type = IdType.AUTO) private Long id; private Long scaleId; private Integer questionNo;
    private String questionContent; private Integer questionType; private String options; private String scoreRule;
    private Integer sortOrder; private LocalDateTime createTime;
}
