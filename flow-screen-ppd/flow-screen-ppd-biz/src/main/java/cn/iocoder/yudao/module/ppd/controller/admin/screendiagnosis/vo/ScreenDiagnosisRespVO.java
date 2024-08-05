package cn.iocoder.yudao.module.ppd.controller.admin.screendiagnosis.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 诊断组 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ScreenDiagnosisRespVO {


    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "10317")
    @ExcelProperty("主键")
    private Long id;

    @Schema(description = "同步时唯一编码", example = "1651")
    @ExcelProperty("同步时唯一编码")
    private Long syncId;

    @Schema(description = "医生签名")
    @ExcelProperty("医生签名")
    private String doctorSignature;

    @Schema(description = "筛查次序")
    @ExcelProperty("筛查次序")
    private Integer screenOrder;

    @Schema(description = "对应摸底表中id", example = "3081")
    @ExcelProperty("对应摸底表中id")
    private Long personId;

    @Schema(description = "备注", example = "你猜")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    // 诊断组列表需要显示的数据

    @Schema(description = "筛查点")
    @ExcelProperty("筛查点")
    private String screenPoint;

    @Schema(description = "筛查编号", example = "4179")
    @ExcelProperty("筛查编号")
    private String screenId;

    /**
     * 第一人群分类（1-重点人群 2-非重点人群 3-教职工）
     */
    private Integer firstType;

    /**
     * 第一人群分类 转化后的字典标签
     */
    private String firstTypeStr;
    /**
     * 姓名
     */
    private String name;
    /**
     * 年龄
     */
    private Integer age;

    @Schema(description = "性别(2-女，1-男)")
    private Integer sex;

    /**
     * 性别 转化后的字典标签
     */
    private String sexStr;

    @Schema(description = "民族")
    private Integer nation;

    /**
     * 民族 转化后的字典标签
     */
    private String nationStr;

    @Schema(description = "身份证号")
    private String idNum;

    @Schema(description = "筛查时间")
    @ExcelProperty("筛查时间")
    private LocalDateTime screenTime;


    // 以下部分为诊断组表中字段 需要显示的为最近一次提交的诊断结果

    @Schema(description = "诊断结果：1-疑似肺结核 2-肺结核 3-肺外结核、4-其他")
    @ExcelProperty("诊断结果：1-疑似肺结核 2-肺结核 3-肺外结核、4-其他")
    private Integer outcome;

    // 诊断结果 转化后的字典标签
    private String outcomeStr;

    @Schema(description = "治疗方案: 1=门诊治疗、2=住院治疗、3=门诊+住院治疗")
    @ExcelProperty("治疗方案: 1=门诊治疗、2=住院治疗、3=门诊+住院治疗")
    private Integer treatmentProgram;

    // 治疗方案 转化后的字典标签
    private String treatmentProgramStr;

    @Schema(description = "是否网报 0-否 1-是")
    @ExcelProperty("是否网报 0-否 1-是")
    private Integer report;

    // 是否网报 转化后的字典标签
    private String reportStr;

    @Schema(description = "符合潜伏治疗条件者是否进行预防性治疗 0-否 1-是")
    @ExcelProperty("符合潜伏治疗条件者是否进行预防性治疗 0-否 1-是")
    private Integer preventiveTreatment;

    // 是否符合潜伏治疗条件者是否进行预防性治疗 转化后的字典标签
    private String preventiveTreatmentStr;

}