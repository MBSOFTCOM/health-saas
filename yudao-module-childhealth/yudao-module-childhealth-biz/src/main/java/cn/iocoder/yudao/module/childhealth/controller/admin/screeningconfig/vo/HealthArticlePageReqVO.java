package cn.iocoder.yudao.module.childhealth.controller.admin.screeningconfig.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 健康文章分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class HealthArticlePageReqVO extends PageParam {

    @Schema(description = "文章编码", example = "ART001")
    private String articleCode;

    @Schema(description = "标题", example = "儿童眼保健指南")
    private String title;

    @Schema(description = "分类（如：眼保健/口腔/营养/心理）", example = "眼保健")
    private String category;

    @Schema(description = "是否置顶 0否 1是", example = "0")
    private Integer isTop;

    @Schema(description = "发布状态 0草稿 1待发布 2已发布 3已下线", example = "2")
    private Integer publishStatus;

    @Schema(description = "作者", example = "李医生")
    private String author;

}
