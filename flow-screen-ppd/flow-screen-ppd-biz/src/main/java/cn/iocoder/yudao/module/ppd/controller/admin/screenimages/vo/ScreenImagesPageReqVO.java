package cn.iocoder.yudao.module.ppd.controller.admin.screenimages.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 移动端各组离线图片信息分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ScreenImagesPageReqVO extends PageParam {

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
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] screenTime;

    @Schema(description = "筛查次序")
    private Integer screenOrder;

    @Schema(description = "筛查点")
    private String screenPoint;

    /**
     * pad上的主键加上身份证号，作为唯一判断
     */
    private Long padId;

}