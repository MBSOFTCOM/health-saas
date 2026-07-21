package cn.iocoder.yudao.module.childhealth.dal.dataobject.ops;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 消息推送配置DO
 */
@Data
@TableName("message_push_config")
public class MessagePushConfigDO {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String configCode;
    private String configName;
    private Integer pushType; // 1催检 2复筛 3随访 4宣教
    private Integer pushChannel; // 1短信 2微信 3APP推送
    private String templateId;
    private String templateContent;
    private String pushRule;
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}