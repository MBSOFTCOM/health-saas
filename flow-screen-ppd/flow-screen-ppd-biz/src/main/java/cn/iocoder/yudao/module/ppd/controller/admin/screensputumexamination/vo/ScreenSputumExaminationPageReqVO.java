package cn.iocoder.yudao.module.ppd.controller.admin.screensputumexamination.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 痰检组分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ScreenSputumExaminationPageReqVO extends PageParam {

    @Schema(description = "筛查编号", example = "28079")
    private String screenId;

    @Schema(description = "同步时唯一编码", example = "11928")
    private Long syncId;

    @Schema(description = "医生签名")
    private String doctorSignature;

    @Schema(description = "是否雾化(0-否，1-是)")
    private Integer atomization;

    @Schema(description = "是否痰检(0-未痰检，1-已经痰检)")
    private Integer sputumExamination;

    @Schema(description = "痰标本类型，1-无痰 2-即时痰 3-发放晨痰 4-夜间痰盒", example = "1")
    private Integer type;

    @Schema(description = "即时痰照片")
    private String forthwithSputum;

    @Schema(description = "即时痰标本号")
    private String forthwithSputumCode;

    @Schema(description = "筛查时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] screenTime;

    @Schema(description = "筛查次序")
    private Integer screenOrder;

    @Schema(description = "对应摸底表中id", example = "25508")
    private Long personId;

    @Schema(description = "痰照片")
    private String eveningSputum;

    @Schema(description = "夜痰标本号")
    private String eveningSputumCode;

    @Schema(description = "晨痰照片")
    private String morningSputum;

    @Schema(description = "晨痰标本号")
    private String morningSputumCode;

    @Schema(description = "结果？？？")
    private String outcome;


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
}