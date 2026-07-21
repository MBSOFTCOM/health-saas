package cn.iocoder.yudao.module.childhealth.controller.admin.referral.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 转介管理分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ReferralRecordPageReqVO extends PageParam {

    @Schema(description = "转介编号", example = "ZJ202401010001")
    private String referralNo;

    @Schema(description = "学生ID", example = "1")
    private Long studentId;

    @Schema(description = "阳性记录ID", example = "1")
    private Long positiveId;

    @Schema(description = "转介状态", example = "0")
    private Integer referralStatus;

}