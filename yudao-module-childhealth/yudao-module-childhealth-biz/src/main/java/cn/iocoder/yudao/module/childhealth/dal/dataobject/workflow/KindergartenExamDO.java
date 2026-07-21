package cn.iocoder.yudao.module.childhealth.dal.dataobject.workflow;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("kindergarten_exam")
public class KindergartenExamDO {
    @TableId(type = IdType.AUTO) private Long id;
    private Long examId;
    private Long childId;
    private LocalDate examDate;
    private String kindergartenName;
    private String examConclusion;
    private Boolean isQualified;
    private Long doctorId;
    private String recordContent;
    private LocalDateTime createTime;
}
