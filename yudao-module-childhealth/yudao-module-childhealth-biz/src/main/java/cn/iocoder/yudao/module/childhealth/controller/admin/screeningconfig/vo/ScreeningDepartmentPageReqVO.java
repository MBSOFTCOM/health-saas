package cn.iocoder.yudao.module.childhealth.controller.admin.screeningconfig.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 筛查科室分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ScreeningDepartmentPageReqVO extends PageParam {

    @Schema(description = "科室编码", example = "DEPT_VISION")
    private String deptCode;

    @Schema(description = "科室名称", example = "视力筛查科")
    private String deptName;

    @Schema(description = "科室类型 视力/口腔/骨骼/心理/体形/综合", example = "视力")
    private String deptType;

    @Schema(description = "负责人姓名", example = "张医生")
    private String principalName;

    @Schema(description = "状态 1启用 0停用", example = "1")
    private Integer status;

}
