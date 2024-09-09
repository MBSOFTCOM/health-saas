package cn.iocoder.yudao.module.ppd.controller.admin.screensum.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 汇总分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ScreenSumPageReqVO extends PageParam {

    @Schema(description = "筛查年份")
    private Integer year;

    @Schema(description = "筛查类型")
    private Integer screenType;

    @Schema(description = "筛查点")
    private String screenPoint;

    @Schema(description = "筛查编号", example = "14440")
    private String screenId;

    @Schema(description = "身份证")
    private String idNum;

    @Schema(description = "对应摸底表中id", example = "4623")
    private Long personId;

    @Schema(description = "同步时唯一编码", example = "8496")
    private Long syncId;

    @Schema(description = "最近一次采集时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] lastCollectTime;

    @Schema(description = "采集次数")
    private Integer collectNum;

    @Schema(description = "最近一次tst时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] lastPpdTime;

    @Schema(description = "tst次数")
    private Integer ppdNum;

    @Schema(description = "最近一次做ct时间")
    private LocalDateTime lastComputedTomographyTime;

    @Schema(description = "ct次数")
    private Integer computedTomographyNum;

    @Schema(description = "最近一次做dr胸片时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] lastChestRadiographTime;

    @Schema(description = "dr次数")
    private Integer chestRadiographNum;

    @Schema(description = "最近一次痰检时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] lastSputumExaminationTime;

    @Schema(description = "痰检次数")
    private Integer sputumExaminationNum;

    @Schema(description = "最近一次心电图时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] lastElectrocardiogramTime;

    @Schema(description = "心电图次数")
    private Integer electrocardiogramNum;

    @Schema(description = "最近一次诊断时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] lastDiagnosisTime;

    @Schema(description = "诊断次数")
    private Integer diagnosisNum;

    @Schema(description = "采集表id", example = "2644")
    private Long collectId;

    @Schema(description = "ppd表id", example = "4570")
    private Long ppdId;

    @Schema(description = "dr胸片表id", example = "10663")
    private Long chestRadiographId;

    @Schema(description = "ct胸片表id", example = "10663")
    private Long computedTomographyId;

    @Schema(description = "痰检表id", example = "18208")
    private Long sputumExaminationId;

    @Schema(description = "心电图表id", example = "23637")
    private Long electrocardiogramId;

    @Schema(description = "诊断表id", example = "16567")
    private Long diagnosisId;

    /**
     * pad上的主键加上身份证号，作为唯一判断
     */
    private Long padId;
}