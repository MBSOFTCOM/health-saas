package cn.iocoder.yudao.module.ppd.controller.admin.screendiagnosis.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ExperimentGroup {

    @Schema(description = "涂片结果（1=阳性、2=阴性、3=未查）")
    @ExcelProperty("涂片结果（1=阳性、2=阴性、3=未查）")
    private Integer smearResult;

    @Schema(description = "培养结果 (1=阳性、2=阴性、3=污染、4=未查）")
    @ExcelProperty("培养结果 (1=阳性、2=阴性、3=污染、4=未查）")
    private Integer cultureResult;

    @Schema(description = "分子生物学（1=结核分枝杆菌核酸阳性、2=未检出结核分枝杆菌、3=不确定、4=未查）")
    @ExcelProperty("分子生物学（1=结核分枝杆菌核酸阳性、2=未检出结核分枝杆菌、3=不确定、4=未查）")
    private Integer molecularBiology;

    @Schema(description = "组织标本检测结果（1=组织学阳性、2=仅病理学阳性、3=阴性、4=未查）")
    @ExcelProperty("组织标本检测结果（1=组织学阳性、2=仅病理学阳性、3=阴性、4=未查）")
    private Integer tissueSpecimenResult;

    @Schema(description = "HIV抗体检测结果（1=已知阳性、2=新检测初筛阳性、3=新检测确认阳性、4=阴性、5=拒查、6=未提供）")
    @ExcelProperty("HIV抗体检测结果（1=已知阳性、2=新检测初筛阳性、3=新检测确认阳性、4=阴性、5=拒查、6=未提供）")
    private Integer hivResult;

    @Schema(description = "菌种鉴定检测结果（1=结核分枝杆菌复合群、2=非结核分枝杆菌、3=未查）")
    private Integer strainIdentificationResult;

    @Schema(description = "结核分支杆菌药敏检测方法（1=分子生物学、2=传统药敏试验）")
    private Integer tbDrugSensitivityMethod;

    @Schema(description = "耐药综合判定（1=单耐利福平、2=耐多药、3=广泛耐药、4=单耐异烟肼、5=利福平与异烟肼均敏感）")
    private Integer drugResistanceResult;
}
