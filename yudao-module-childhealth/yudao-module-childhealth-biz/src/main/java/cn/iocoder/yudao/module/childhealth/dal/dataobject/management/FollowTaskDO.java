package cn.iocoder.yudao.module.childhealth.dal.dataobject.management;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.*;

@Data
@TableName("follow_task")
public class FollowTaskDO {
    @TableId(type = IdType.AUTO) private Long id;
    private String taskNo;
    private Long childId;
    private Integer taskType;
    private String taskSource;
    private String taskContent;
    private Integer priority;
    private LocalDate planDate;
    private Long responsibleDoctor;
    private Integer taskStatus;
    private LocalDateTime completeTime;
    private LocalDateTime createTime;
}
