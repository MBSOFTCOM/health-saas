package cn.iocoder.yudao.module.ppd.controller.admin.screenexperiment.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - 实验室组 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ScreenExperimentRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "17051")
    @ExcelProperty("主键")
    private Long id;

    @Schema(description = "同步时唯一编码", example = "12260")
    @ExcelProperty("同步时唯一编码")
    private Long syncId;

    @Schema(description = "对应摸底表中id", example = "3884")
    @ExcelProperty("对应摸底表中id")
    private Long personId;

    @Schema(description = "筛查编号", example = "14019")
    @ExcelProperty("筛查编号")
    private String screenId;

    @Schema(description = "对应痰检组表中id", example = "11563")
    @ExcelProperty("对应痰检组表中id")
    private Long sputumExaminationId;

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

    @Schema(description = "筛查次序/时间")
    @ExcelProperty("筛查次序/时间")
    private List<ScreenOrderValue> screenOrderValues;

    @Schema(description = "筛查时间")
    @ExcelProperty("筛查时间")
    private LocalDateTime screenTime;

    @Schema(description = "筛查次序")
    @ExcelProperty("筛查次序")
    private Integer screenOrder;

    /**
     * 移动端下拉框显示时间最近一次次序
     */
    private Integer orderVal;

    /**
     * 姓名
     */
    private String name;
    /**
     * 身份证号
     */
    private String idNum;
    /**
     * 性别(2-女，1-男)
     *
     * 枚举 {system_user_sex 对应的类}
     */
    private Integer sex;
    /**
     * 性别(2-女，1-男) 兼容移动端属性
     *
     * 枚举 {system_user_sex 对应的类}
     */
    private String sexStr;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 民族
     */
    private Integer nation;
    /**
     * 民族 兼容移动端属性
     */
    private String nationStr;
    /**
     * 第一人群分类（1-重点人群 2-非重点人群 3-教职工）
     */
    private Integer firstType;
    /**
     * 第一人群分类 兼容移动端属性
     */
    private String firstTypeStr;
    /**
     * 下一次操作的组名
     */
    private String showNextStr;
}