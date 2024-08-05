package cn.iocoder.yudao.module.ppd.controller.admin.screenchestradiograph.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - ct、dr组 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ScreenChestRadiographDownloadRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "16389")
    @ExcelProperty("主键")
    private Long id;

    @Schema(description = "筛查编号", example = "665")
    @ExcelProperty("筛查编号")
    private String screenId;

    @Schema(description = "同步时唯一编码", example = "2243")
    @ExcelProperty("同步时唯一编码")
    private Long syncId;

    @Schema(description = "胸片编号")
    @ExcelProperty("胸片编号")
    private String chestRadiographCode;

    @Schema(description = "胸片")
    @ExcelProperty("胸片")
    private String chestRadiograph;

    @Schema(description = "医生签名")
    @ExcelProperty("医生签名")
    private String doctorSignature;

    @Schema(description = "筛查时间")
    @ExcelProperty("筛查时间")
    private String screenTime;

    @Schema(description = "筛查次序")
    @ExcelProperty("筛查次序")
    private Integer screenOrder;

    @Schema(description = "对应摸底表中id", example = "4833")
    @ExcelProperty("对应摸底表中id")
    private Long personId;

    @Schema(description = "结果。2-其他异常 1-疑似结核 0-无异常")
    @ExcelProperty("结果。2-其他异常 1-疑似结核 0-无异常")
    private Integer outcome;

    @Schema(description = "其他异常说明", example = "你猜")
    @ExcelProperty("其他异常说明")
    private String remark;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 筛查类型
     */
    private Integer screenType;

    /**
     * 对应摸底表中患者姓名
     */
    private String name;

    /**
     * 拍照时间
     */
    private LocalDateTime photoTime;
}