package cn.iocoder.yudao.module.ppd.controller.admin.screensputumexamination.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 痰检组 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ScreenSputumExaminationRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "18387")
    @ExcelProperty("主键")
    private Long id;

    @Schema(description = "筛查编号", example = "28079")
    @ExcelProperty("筛查编号")
    private String screenId;

    @Schema(description = "同步时唯一编码", example = "11928")
    @ExcelProperty("同步时唯一编码")
    private Long syncId;

    @Schema(description = "医生签名")
    @ExcelProperty("医生签名")
    private String doctorSignature;

    @Schema(description = "是否雾化(0-否，1-是)")
    @ExcelProperty("是否雾化(0-否，1-是)")
    private Integer atomization;

    @Schema(description = "是否痰检(0-未痰检，1-已经痰检)")
    @ExcelProperty("是否痰检(0-未痰检，1-已经痰检)")
    private Integer sputumExamination;

    @Schema(description = "痰标本类型，1-无痰 2-即时痰 3-发放晨痰 4-夜间痰盒", example = "1")
    @ExcelProperty("痰标本类型，1-无痰 2-即时痰 3-发放晨痰 4-夜间痰盒")
    private Integer type;

    @Schema(description = "即时痰照片")
    @ExcelProperty("即时痰照片")
    private String forthwithSputum;

    @Schema(description = "即时痰标本号")
    @ExcelProperty("即时痰标本号")
    private String forthwithSputumCode;

    @Schema(description = "筛查时间")
    @ExcelProperty("筛查时间")
    private LocalDateTime screenTime;

    @Schema(description = "筛查次序")
    @ExcelProperty("筛查次序")
    private Integer screenOrder;

    @Schema(description = "对应摸底表中id", example = "25508")
    @ExcelProperty("对应摸底表中id")
    private Long personId;

    @Schema(description = "痰照片")
    @ExcelProperty("痰照片")
    private String eveningSputum;

    @Schema(description = "夜痰标本号")
    @ExcelProperty("夜痰标本号")
    private String eveningSputumCode;

    @Schema(description = "晨痰照片")
    @ExcelProperty("晨痰照片")
    private String morningSputum;

    @Schema(description = "晨痰标本号")
    @ExcelProperty("晨痰标本号")
    private String morningSputumCode;

    @Schema(description = "结果？？？")
    @ExcelProperty("结果？？？")
    private String outcome;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 筛查类型
     */
    private Integer screenType;

    /**
     * 对应摸底表中患者姓名
     */
    private String name;

    /**
     * 即时痰照片采集时间
     */
    private LocalDateTime forthwithSputumTime;
    /**
     * 夜痰照片采集时间
     */
    private LocalDateTime eveningSputumTime;
    /**
     * 晨痰照片采集时间
     */
    private LocalDateTime morningSputumTime;

}