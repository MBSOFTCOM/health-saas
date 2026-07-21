package cn.iocoder.yudao.module.childhealth.controller.admin.screeningconfig.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 健康文章新增/修改 Request VO")
@Data
public class HealthArticleSaveReqVO {

    @Schema(description = "主键ID", example = "1")
    private Long id;

    @Schema(description = "文章编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "ART001")
    @NotBlank(message = "文章编码不能为空")
    private String articleCode;

    @Schema(description = "标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "儿童眼保健指南")
    @NotBlank(message = "标题不能为空")
    private String title;

    @Schema(description = "分类（如：眼保健/口腔/营养/心理）", example = "眼保健")
    private String category;

    @Schema(description = "封面图URL")
    private String coverUrl;

    @Schema(description = "摘要")
    private String summary;

    @Schema(description = "正文（HTML）")
    private String content;

    @Schema(description = "标签JSON")
    private String tags;

    @Schema(description = "是否置顶 0否 1是", example = "0")
    private Integer isTop;

    @Schema(description = "发布状态 0草稿 1待发布 2已发布 3已下线", example = "0")
    private Integer publishStatus;

    @Schema(description = "发布时间（定时发布）")
    private LocalDateTime publishTime;

    @Schema(description = "作者", example = "李医生")
    private String author;

    @Schema(description = "浏览次数", example = "0")
    private Integer viewCount;

    @Schema(description = "点赞数", example = "0")
    private Integer likeCount;

    @Schema(description = "适用疾病编码JSON（精准推送）")
    private String applicableDisease;

}
