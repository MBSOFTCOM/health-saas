package cn.iocoder.yudao.module.ppd.controller.admin.screenimages.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 移动端各组离线图片信息新增/修改 Request VO")
@Data
public class ScreenImagesSaveReqVO {

    @Schema(description = "自增主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "25892")
    private Long id;

    @Schema(description = "同步时唯一编码", example = "14015")
    private Long syncId;

    @Schema(description = "筛查编号", example = "11681")
    private String screenId;

    @Schema(description = "对应摸底表中id", example = "10261")
    private Long personId;

    @Schema(description = "图片来源，1-DR 2-CT 3-实验室-痰菌培养图 4-心电图	5-即时痰、6-晨痰、7-夜间痰", example = "1")
    private Integer type;

    @Schema(description = "图片路径（平板本地路径，保存在 /doc/..中）")
    private String path;

    @Schema(description = "网络图片路径（完整路径）", example = "https://www.iocoder.cn")
    private String url;

    @Schema(description = "筛查时间")
    private String screenTime;

    @Schema(description = "筛查次序")
    private Integer screenOrder;

    @Schema(description = "筛查点")
    private String screenPoint;

    @Schema(description = "年份")
    private Integer year;

    @Schema(description = "筛查类型")
    private Integer screenType;
}