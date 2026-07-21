package cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.item;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 筛查项目配置列表 Request VO")
@Data
public class ScreeningItemConfigListReqVO {

    @Schema(description = "项目编码", example = "ITEM001")
    private String itemCode;

    @Schema(description = "项目名称", example = "视力")
    private String itemName;

    @Schema(description = "所属类别 体形/视力/口腔/骨骼/心理", example = "视力")
    private String category;

    @Schema(description = "状态 1正常 0停用", example = "1")
    private Integer status;

}