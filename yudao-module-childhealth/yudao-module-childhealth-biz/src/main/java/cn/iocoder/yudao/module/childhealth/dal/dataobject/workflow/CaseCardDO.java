package cn.iocoder.yudao.module.childhealth.dal.dataobject.workflow;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("case_card")
public class CaseCardDO {
    @TableId(type = IdType.AUTO) private Long id;
    private Long caseId;
    private String cardContent;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
