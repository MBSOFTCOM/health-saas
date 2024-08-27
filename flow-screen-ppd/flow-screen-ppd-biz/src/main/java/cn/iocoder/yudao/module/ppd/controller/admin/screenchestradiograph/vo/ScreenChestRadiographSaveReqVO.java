package cn.iocoder.yudao.module.ppd.controller.admin.screenchestradiograph.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - ct、dr组新增/修改 Request VO")
@Data
public class ScreenChestRadiographSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "16389")
    private Long id;

    @Schema(description = "筛查编号", example = "665")
    private String screenId;

    @Schema(description = "身份证")
    private String idNum;

    @Schema(description = "同步时唯一编码", example = "2243")
    private Long syncId;

    @Schema(description = "胸片编号")
    private String chestRadiographCode;

    @Schema(description = "胸片")
    private String chestRadiograph;

    @Schema(description = "医生签名")
    private String doctorSignature;

    @Schema(description = "筛查时间")
    private LocalDateTime screenTime;

    @Schema(description = "拍照时间")
    private LocalDateTime photoTime;

    @Schema(description = "筛查次序")
    private Integer screenOrder;

    @Schema(description = "对应摸底表中id", example = "4833")
    private Long personId;

    @Schema(description = "结果。2-其他异常 1-疑似结核 0-无异常")
    private Integer outcome;

    @Schema(description = "筛查年份")
    private Integer year;

    @Schema(description = "筛查类型。1--常规、2--新生、3--应急")
    private Integer screenType;

    @Schema(description = "其他异常说明", example = "你猜")
    private String remark;
    private Integer statusFlag;

}