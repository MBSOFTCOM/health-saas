package cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.item;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 筛查项目配置 Response VO")
@Data
public class ScreeningItemConfigRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "项目编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "ITEM001")
    private String itemCode;

    @Schema(description = "项目名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "视力")
    private String itemName;

    @Schema(description = "所属类别 体形/视力/口腔/骨骼/心理", example = "视力")
    private String category;

    @Schema(description = "结果类型 1数值 2选项 3多选", example = "1")
    private Integer resultType;

    @Schema(description = "单位", example = "cm")
    private String unit;

    @Schema(description = "参考区间JSON")
    private String referenceRange;

    @Schema(description = "适用年龄")
    private String applicableAge;

    @Schema(description = "适用性别 0不限 1男 2女", example = "0")
    private Integer applicableGender;

    @Schema(description = "排序号", example = "1")
    private Integer sortOrder;

    @Schema(description = "状态 1正常 0停用", example = "1")
    private Integer status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}