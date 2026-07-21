package cn.iocoder.yudao.module.childhealth.controller.admin.casemanagement.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 高危儿随访分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class HighRiskFollowupPageReqVO extends PageParam {

    @Schema(description = "高危新生儿ID", example = "1024")
    private Long newbornId;

    @Schema(description = "儿童ID", example = "1024")
    private Long childId;

    @Schema(description = "随访编号", example = "HF20260720001")
    private String followupNo;

    @Schema(description = "随访类型 1院内 2家庭 3电话", example = "1")
    private Integer followupType;

    @Schema(description = "医生ID", example = "1")
    private Long doctorId;

}
