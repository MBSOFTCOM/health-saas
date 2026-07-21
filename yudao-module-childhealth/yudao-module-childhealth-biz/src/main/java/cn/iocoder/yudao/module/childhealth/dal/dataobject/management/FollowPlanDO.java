package cn.iocoder.yudao.module.childhealth.dal.dataobject.management;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.*;

@Data
@TableName("follow_plan")
public class FollowPlanDO {
    @TableId(type = IdType.AUTO) private Long id;
    private Long caseId;
    private Integer planType;
    private LocalDate planDate;
    private String followContent;
    private Integer planStatus;
    private LocalDate executeDate;
    private LocalDateTime createTime;
}
