package cn.iocoder.yudao.module.childhealth.controller.admin.casemanagement.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 高危新生儿台账分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class HighRiskNewbornPageReqVO extends PageParam {

    @Schema(description = "儿童档案ID", example = "1024")
    private Long childId;

    @Schema(description = "母亲姓名", example = "李梅")
    private String motherName;

    @Schema(description = "高危类型JSON")
    private String highRiskTypes;

    @Schema(description = "是否已建册 0否 1是", example = "1")
    private Integer isRegistered;

    @Schema(description = "随访状态 0未随访 1随访中 2已结案", example = "0")
    private Integer followupStatus;

    @Schema(description = "关联专案ID", example = "1")
    private Long caseId;

}
