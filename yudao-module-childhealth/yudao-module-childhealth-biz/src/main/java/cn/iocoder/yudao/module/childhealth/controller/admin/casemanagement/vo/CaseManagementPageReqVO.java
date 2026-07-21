package cn.iocoder.yudao.module.childhealth.controller.admin.casemanagement.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 专案主表分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CaseManagementPageReqVO extends PageParam {

    @Schema(description = "专案编号", example = "CASE20260720001")
    private String caseNo;

    @Schema(description = "儿童ID", example = "1024")
    private Long childId;

    @Schema(description = "专案类型 1高危儿 2营养不良 3肥胖 4贫血 5佝偻病 6发育行为异常", example = "1")
    private Integer caseType;

    @Schema(description = "专案子类型")
    private String caseSubtype;

    @Schema(description = "建立方式 0自动 1手动", example = "1")
    private Integer caseSource;

    @Schema(description = "专案等级 1轻度 2中度 3重度", example = "1")
    private Integer caseLevel;

    @Schema(description = "状态 0进行中 1已结案 2已取消", example = "0")
    private Integer status;

    @Schema(description = "责任医生ID", example = "1")
    private Long responsibleDoctorId;

}
