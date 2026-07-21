package cn.iocoder.yudao.module.childhealth.controller.admin.casemanagement.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Schema(description = "管理后台 - 专案主表新增/修改 Request VO")
@Data
public class CaseManagementSaveReqVO {

    @Schema(description = "主键ID", example = "1")
    private Long id;

    @Schema(description = "专案编号", example = "CASE20260720001")
    private String caseNo;

    @Schema(description = "儿童ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "儿童ID不能为空")
    private Long childId;

    @Schema(description = "专案类型 1高危儿 2营养不良 3肥胖 4贫血 5佝偻病 6发育行为异常", example = "1")
    private Integer caseType;

    @Schema(description = "专案子类型（如：早产儿/低体重/高胆红素血症/遗传代谢/HIE/生长迟缓/消瘦/超重等）")
    private String caseSubtype;

    @Schema(description = "建立方式 0自动 1手动", example = "1")
    private Integer caseSource;

    @Schema(description = "来源记录ID（体检/筛查/评估）", example = "1")
    private Long sourceRecordId;

    @Schema(description = "专案等级 1轻度 2中度 3重度", example = "1")
    private Integer caseLevel;

    @Schema(description = "标签JSON")
    private String caseTags;

    @Schema(description = "建立日期")
    private LocalDate establishDate;

    @Schema(description = "结案日期")
    private LocalDate closeDate;

    @Schema(description = "结案原因")
    private String closeReason;

    @Schema(description = "结案类型 1康复达标 2转介 3其他", example = "1")
    private Integer closeType;

    @Schema(description = "状态 0进行中 1已结案 2已取消", example = "0")
    private Integer status;

    @Schema(description = "责任医生ID", example = "1")
    private Long responsibleDoctorId;

    @Schema(description = "责任医生姓名", example = "张医生")
    private String responsibleDoctorName;

    @Schema(description = "个案卡文件URL")
    private String caseCardUrl;

    @Schema(description = "专案描述")
    private String description;

}
