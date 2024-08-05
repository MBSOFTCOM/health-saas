package cn.iocoder.yudao.module.ppd.controller.admin.screenexperiment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 实验室组新增/修改 Request VO")
@Data
public class ScreenExperimentSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "17051")
    private Long id;

    @Schema(description = "同步时唯一编码", example = "12260")
    private Long syncId;

    @Schema(description = "对应摸底表中id", example = "3884")
    private Long personId;

    @Schema(description = "对应痰检组表中id", example = "11563")
    private Long sputumExaminationId;

    @Schema(description = "涂片结果（1=阳性、2=阴性、3=未查）")
    private Integer smearResult;

    @Schema(description = "培养结果 (1=阳性、2=阴性、3=污染、4=未查）")
    private Integer cultureResult;

    @Schema(description = "分子生物学（1=结核分枝杆菌核酸阳性、2=未检出结核分枝杆菌、3=不确定、4=未查）")
    private Integer molecularBiology;

    @Schema(description = "组织标本检测结果（1=组织学阳性、2=仅病理学阳性、3=阴性、4=未查）")
    private Integer tissueSpecimenResult;

    @Schema(description = "菌种鉴定检测结果（1=结核分枝杆菌复合群、2=非结核分枝杆菌、3=未查）")
    private Integer strainIdentificationResult;

    @Schema(description = "结核分支杆菌药敏检测方法（1=分子生物学、2=传统药敏试验）")
    private Integer tbDrugSensitivityMethod;

    @Schema(description = "耐药综合判定（1=单耐利福平、2=耐多药、3=广泛耐药、4=单耐异烟肼、5=利福平与异烟肼均敏感）")
    private Integer drugResistanceResult;

    @Schema(description = "HIV抗体检测结果（1=已知阳性、2=新检测初筛阳性、3=新检测确认阳性、4=阴性、5=拒查、6=未提供）")
    private Integer hivResult;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

    /**
     * 筛查时间
     */
    private LocalDateTime screenTime;
    /**
     * 筛查次序
     */
    private Integer screenOrder;
    /**
     * 筛查编号
     */
    private String screenId;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 筛查类型  1--常规、2--新生、3--应急
     */
    private Integer screenType;
}