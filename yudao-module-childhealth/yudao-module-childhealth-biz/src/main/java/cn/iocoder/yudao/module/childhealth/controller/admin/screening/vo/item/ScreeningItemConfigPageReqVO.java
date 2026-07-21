package cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.item;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 筛查项目配置分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ScreeningItemConfigPageReqVO extends PageParam {

    @Schema(description = "项目编码", example = "ITEM001")
    private String itemCode;

    @Schema(description = "项目名称", example = "视力")
    private String itemName;

    @Schema(description = "所属类别 体形/视力/口腔/骨骼/心理", example = "视力")
    private String category;

    @Schema(description = "结果类型 1数值 2选项 3多选", example = "1")
    private Integer resultType;

    @Schema(description = "状态 1正常 0停用", example = "1")
    private Integer status;

}