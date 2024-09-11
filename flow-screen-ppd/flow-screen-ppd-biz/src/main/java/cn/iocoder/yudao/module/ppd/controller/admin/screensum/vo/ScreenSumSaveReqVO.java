package cn.iocoder.yudao.module.ppd.controller.admin.screensum.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 汇总新增/修改 Request VO")
@Data
public class ScreenSumSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "31")
    private Long id;

    @Schema(description = "筛查年份")
    private String  year;

    @Schema(description = "筛查编号", example = "14440")
    private String screenId;

    @Schema(description = "筛查类型", example = "1")
    private Integer screenType;

    @Schema(description = "身份证")
    private String idNum;

    @Schema(description = "对应摸底表中id", example = "4623")
    private Long personId;

    @Schema(description = "同步时唯一编码", example = "8496")
    private Long syncId;

    @Schema(description = "最近一次采集时间")
    private LocalDateTime lastCollectTime;

    @Schema(description = "采集次数")
    private Integer collectNum;

    @Schema(description = "最近一次tst时间")
    private LocalDateTime lastPpdTime;

    @Schema(description = "tst次数")
    private Integer ppdNum;

    @Schema(description = "最近一次做ct时间")
    private LocalDateTime lastComputedTomographyTime;

    @Schema(description = "ct次数")
    private Integer computedTomographyNum;

    @Schema(description = "最近一次做dr时间")
    private LocalDateTime lastChestRadiographTime;

    @Schema(description = "dr次数")
    private Integer chestRadiographNum;

    @Schema(description = "最近一次痰检时间")
    private LocalDateTime lastSputumExaminationTime;

    @Schema(description = "痰检次数")
    private Integer sputumExaminationNum;

    @Schema(description = "最近一次心电图时间")
    private LocalDateTime lastElectrocardiogramTime;

    @Schema(description = "心电图次数")
    private Integer electrocardiogramNum;

    @Schema(description = "最近一次诊断时间")
    private LocalDateTime lastDiagnosisTime;

    @Schema(description = "诊断次数")
    private Integer diagnosisNum;

    @Schema(description = "当前所完成的分组")
    private String curFinish;

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
     * 最近一次实验组时间
     */
    private LocalDateTime lastExperimentTime;
    /**
     * 实验次数
     */
    private Integer experimentNum;
    /**
     * 实验组id
     */
    private Long experimentId;

    private String padId;
}