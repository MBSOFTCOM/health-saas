package cn.iocoder.yudao.module.childhealth.controller.admin.casemanagement.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 体检催检推送日志分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ExamReminderLogPageReqVO extends PageParam {

    @Schema(description = "规则ID", example = "1024")
    private Long ruleId;

    @Schema(description = "儿童ID", example = "1024")
    private Long childId;

    @Schema(description = "推送渠道")
    private String pushChannel;

    @Schema(description = "状态 0待发送 1成功 2失败 3已取消 4已补发", example = "0")
    private Integer pushStatus;

}
