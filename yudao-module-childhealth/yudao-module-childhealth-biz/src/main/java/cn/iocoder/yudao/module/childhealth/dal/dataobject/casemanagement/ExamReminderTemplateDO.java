package cn.iocoder.yudao.module.childhealth.dal.dataobject.casemanagement;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 体检提醒模板表 DO
 *
 * 对应表: exam_reminder_template
 * 模块: B.体检催检
 * 创建日期: 2026-07-20
 */
@TableName("exam_reminder_template")
@KeySequence("exam_reminder_template_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamReminderTemplateDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 模板编码
     */
    private String templateCode;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 模板内容（支持变量占位符）
     */
    private String templateContent;

    /**
     * 模板类型 1满月 2常规 3入园入托 4专案 5科室自定义
     */
    private Integer templateType;

    /**
     * 变量定义JSON
     */
    private String variablesJson;

    /**
     * 科室ID
     */
    private Long deptId;

    /**
     * 推送渠道 sms/wechat/app
     */
    private String channel;

    /**
     * 状态 1启用 0停用
     */
    private Integer status;

}
