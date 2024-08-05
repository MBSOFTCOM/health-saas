package cn.iocoder.yudao.module.ppd.controller.admin.screenimages.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 移动端各组离线图片信息 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ScreenImagesRespVO {

    @Schema(description = "自增主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "25892")
    @ExcelProperty("自增主键")
    private Long id;

    @Schema(description = "同步时唯一编码", example = "14015")
    @ExcelProperty("同步时唯一编码")
    private Long syncId;

    @Schema(description = "筛查编号", example = "11681")
    @ExcelProperty("筛查编号")
    private String screenId;

    @Schema(description = "对应摸底表中id", example = "10261")
    @ExcelProperty("对应摸底表中id")
    private Long personId;

    @Schema(description = "图片来源，1-DR 2-CT 3-实验室-痰菌培养图 4-心电图	5-即时痰、6-晨痰、7-夜间痰", example = "1")
    @ExcelProperty("图片来源，1-DR 2-CT 3-实验室-痰菌培养图 4-心电图	5-即时痰、6-晨痰、7-夜间痰")
    private Integer type;

    @Schema(description = "图片路径（平板本地路径，保存在 /doc/..中）")
    @ExcelProperty("图片路径（平板本地路径，保存在 /doc/..中）")
    private String path;

    @Schema(description = "网络图片路径（完整路径）", example = "https://www.iocoder.cn")
    @ExcelProperty("网络图片路径（完整路径）")
    private String url;

    @Schema(description = "筛查时间")
    @ExcelProperty("筛查时间")
    private LocalDateTime screenTime;

    @Schema(description = "筛查次序")
    @ExcelProperty("筛查次序")
    private Integer screenOrder;

    @Schema(description = "筛查点")
    @ExcelProperty("筛查点")
    private String screenPoint;

}