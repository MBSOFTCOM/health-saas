package cn.iocoder.yudao.module.childhealth.controller.admin.screeningconfig.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Schema(description = "管理后台 - 筛查科室新增/修改 Request VO")
@Data
public class ScreeningDepartmentSaveReqVO {

    @Schema(description = "主键ID", example = "1")
    private Long id;

    @Schema(description = "科室编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "DEPT_VISION")
    @NotBlank(message = "科室编码不能为空")
    private String deptCode;

    @Schema(description = "科室名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "视力筛查科")
    @NotBlank(message = "科室名称不能为空")
    private String deptName;

    @Schema(description = "科室类型 视力/口腔/骨骼/心理/体形/综合", example = "视力")
    private String deptType;

    @Schema(description = "绑定体检项目JSON")
    private String bindItemsJson;

    @Schema(description = "绑定筛查任务JSON")
    private String bindTasksJson;

    @Schema(description = "业务统计JSON")
    private String statisticsJson;

    @Schema(description = "负责人ID", example = "1001")
    private Long principalId;

    @Schema(description = "负责人姓名", example = "张医生")
    private String principalName;

    @Schema(description = "联系电话", example = "13800000000")
    private String phone;

    @Schema(description = "排序", example = "1")
    private Integer sort;

    @Schema(description = "状态 1启用 0停用", example = "1")
    private Integer status;

}
