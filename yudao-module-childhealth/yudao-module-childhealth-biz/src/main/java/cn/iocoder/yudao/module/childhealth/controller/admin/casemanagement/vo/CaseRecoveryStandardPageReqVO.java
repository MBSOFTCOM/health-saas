package cn.iocoder.yudao.module.childhealth.controller.admin.casemanagement.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 专案康复达标标准分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CaseRecoveryStandardPageReqVO extends PageParam {

    @Schema(description = "专案类型", example = "1")
    private Integer caseType;

    @Schema(description = "专案子类型")
    private String caseSubtype;

    @Schema(description = "指标编码", example = "WEIGHT")
    private String indicatorCode;

    @Schema(description = "指标名称", example = "体重")
    private String indicatorName;

    @Schema(description = "状态 1启用 0停用", example = "1")
    private Integer status;

}
