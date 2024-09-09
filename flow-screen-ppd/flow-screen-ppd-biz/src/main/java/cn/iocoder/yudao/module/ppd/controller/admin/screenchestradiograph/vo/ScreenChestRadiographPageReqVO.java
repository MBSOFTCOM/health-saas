package cn.iocoder.yudao.module.ppd.controller.admin.screenchestradiograph.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - ct、dr组分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ScreenChestRadiographPageReqVO extends PageParam {

    @Schema(description = "筛查编号", example = "665")
    private String screenId;

    @Schema(description = "同步时唯一编码", example = "2243")
    private Long syncId;

    @Schema(description = "胸片编号")
    private String chestRadiographCode;

    @Schema(description = "胸片")
    private String chestRadiograph;

    @Schema(description = "胸片类型")
    private Integer type;

    @Schema(description = "医生签名")
    private String doctorSignature;

    @Schema(description = "筛查时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] screenTime;

    @Schema(description = "筛查次序")
    private Integer screenOrder;

    @Schema(description = "对应摸底表中id", example = "4833")
    private Long personId;

    @Schema(description = "结果。2-其他异常 1-疑似结核 0-无异常")
    private Integer outcome;

    @Schema(description = "其他异常说明", example = "你猜")
    private String remark;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 筛查类型
     */
    private Integer screenType;

    @Schema(description = "筛查点")
    private String screenPoint;

    /**
     * pad上的主键加上身份证号，作为唯一判断
     */
    private Long padId;
}