package cn.iocoder.yudao.module.ppd.controller.admin.screencomputedtomography.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - ct、dr组 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ScreenComputedTomographyPersonRespVO {

    @Schema(description = "身份证号")
    private String idNum;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "年龄")
    private Integer age;

    @Schema(description = "联系电话·")
    private String tel;

    @Schema(description = "性别(2-女，1-男)")
    private Integer sex;

    @Schema(description = "民族")
    private Integer nation;

    @Schema(description = "筛查点")
    private String screenPoint;

    @Schema(description = "筛查类型")
    private Integer screenType;

    @Schema(description = "筛查年度")
    private Integer year;

    @Schema(description = "第一人群分类（1-重点人群 2-非重点人群 3-教职工）")
    private Integer firstType;

    @Schema(description = "多人群分类（1-学生、2-老年人、3-教职工、4-密接者、5-糖尿病、6-僧尼、7-既往患者）")
    private Integer moreType;

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "16389")
    @ExcelProperty("主键")
    private Long id;

    @Schema(description = "筛查编号", example = "665")
    @ExcelProperty("筛查编号")
    private String screenId;

    @Schema(description = "同步时唯一编码", example = "2243")
    @ExcelProperty("同步时唯一编码")
    private Long syncId;

    @Schema(description = "筛查时间")
    @ExcelProperty("筛查时间")
    private LocalDateTime screenTime;

    @Schema(description = "筛查次序")
    @ExcelProperty("筛查次序")
    private Integer screenOrder;

    @Schema(description = "已完成分组")
    @ExcelProperty("已完成分组")
    private String  curFinish;

    @Schema(description = "对应摸底表中id", example = "4833")
    @ExcelProperty("对应摸底表中id")
    private Long personId;

}