package cn.iocoder.yudao.module.ppd.controller.admin.screencomputedtomography.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - ct、dr组分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ScreenComputedTomographyPersonPageReqVO extends PageParam {

    @Schema(description = "筛查编号", example = "665")
    private String screenId;

    @Schema(description = "患者姓名", example = "665")
    private String name;

    @Schema(description = "同步时唯一编码", example = "2243")
    private Long syncId;

    @Schema(description = "患者身份证", example = "2243")
    private String idNum;

    @Schema(description = "第一人群分类")
    private List<Integer> firstType;

    @Schema(description = "多人群分类")
    private List<Integer> moreType;

    @Schema(description = "多人群分类")
    private Integer moreTypeNum;

    @Schema(description = "筛查开始时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private String  startTime;

    @Schema(description = "筛查结束时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private String endTime;

    @Schema(description = "筛查次序")
    private Integer screenOrder;

    @Schema(description = "对应摸底表中id", example = "4833")
    private Long personId;

    @Schema(description = "结果。2-其他异常 1-疑似结核 0-无异常")
    private Integer outcome;

    @Schema(description = "其他异常说明", example = "你猜")
    private String remark;

    @Schema(description = "筛查类型", example = "1")
    @ExcelProperty("筛查类型  1--常规、2--新生、3--应急")
    private Integer screenType;

    @Schema(description = "筛查年度", example = "2023")
    @ExcelProperty("筛查年度")
    private Integer year;

}