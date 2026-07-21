package cn.iocoder.yudao.module.childhealth.controller.admin.casemanagement.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 专案随访记录分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CaseFollowupRecordPageReqVO extends PageParam {

    @Schema(description = "专案ID", example = "1024")
    private Long caseId;

    @Schema(description = "儿童ID", example = "1024")
    private Long childId;

    @Schema(description = "随访编号", example = "CF20260720001")
    private String followupNo;

    @Schema(description = "随访类型 1面诊 2电话 3短信 4微信 5线上问卷", example = "1")
    private Integer followupType;

    @Schema(description = "健康变化 1改善 2稳定 3恶化", example = "1")
    private Integer healthChange;

    @Schema(description = "随访医生ID", example = "1")
    private Long doctorId;

}
