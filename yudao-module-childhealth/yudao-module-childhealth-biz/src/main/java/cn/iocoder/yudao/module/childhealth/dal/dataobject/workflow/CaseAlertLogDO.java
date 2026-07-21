package cn.iocoder.yudao.module.childhealth.dal.dataobject.workflow;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("case_alert_log")
public class CaseAlertLogDO {
    @TableId(type = IdType.AUTO) private Long id;
    private Long examId;
    private Long childId;
    private String alertType;
    /** 提醒级别 WARNING/DANGER */
    private String alertLevel;
    /** 触发项目 */
    private String triggerItem;
    /** 触发值 */
    private String triggerValue;
    /** 阈值规则 */
    private String thresholdRule;
    /** 关联专案ID */
    private Long caseId;
    private String alertContent;
    private String suggestCaseType;
    private Boolean isHandled;
    private String handleResult;
    /** 处理动作 CREATE_CASE/IGNORE */
    private String handleAction;
    /** 处理人ID */
    private Long handleUserId;
    /** 处理时间 */
    private LocalDateTime handleTime;
    /** 处理备注 */
    private String handleRemark;
    private LocalDateTime createTime;
}
