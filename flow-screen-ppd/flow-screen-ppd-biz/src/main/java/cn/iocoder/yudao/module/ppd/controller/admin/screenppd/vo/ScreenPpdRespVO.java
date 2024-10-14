package cn.iocoder.yudao.module.ppd.controller.admin.screenppd.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - ppd组记录 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ScreenPpdRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "26824")
    @ExcelProperty("主键")
    private Long id;

    @Schema(description = "筛查编号(生成的编码)", example = "2195")
    @ExcelProperty("筛查编号(生成的编码)")
    private String screenId;

    @Schema(description = "同步时唯一编码", example = "9954")
    @ExcelProperty("同步时唯一编码")
    private Long syncId;

    @Schema(description = "对应摸底表中id", example = "2249")
    @ExcelProperty("对应摸底表中id")
    private Long personId;

    @Schema(description = "横径 单位mm")
    @ExcelProperty("横径 单位mm")
    private BigDecimal transverseDiameter;

    @Schema(description = "纵径 单位mm")
    @ExcelProperty("纵径 单位mm")
    private BigDecimal longitudinalDiameter;

    @Schema(description = "横径 单位mm")
    @ExcelProperty("横径 单位mm")
    private BigDecimal blushTransverseDiameter;

    @Schema(description = "纵径 单位mm")
    @ExcelProperty("纵径 单位mm")
    private BigDecimal blushLongitudinalDiameter;

    @Schema(description = "是否含有水泡/双圈/坏死/淋巴管炎/ 1-是 0-否")
    @ExcelProperty("是否含有水泡/双圈/坏死/淋巴管炎/ 1-是 0-否")
    private Integer bleb;

    @Schema(description = "是否注射(1-是 0-否)")
    @ExcelProperty("是否注射(1-是 0-否)")
    private Integer injection;

    @Schema(description = "注射方式（根据字典）")
    @ExcelProperty("注射方式（根据字典）")
    private Integer injectionWay;

    @Schema(description = "结果。1-感染 0-未感染")
    @ExcelProperty("结果。1-感染 0-未感染")
    private Integer outcome;

    @Schema(description = "医生签名")
    @ExcelProperty("医生签名")
    private String doctorSignature;

    @Schema(description = "注射单位")
    @ExcelProperty("注射单位")
    private String injectionAgency;

    @Schema(description = "筛查次序")
    @ExcelProperty("筛查次序")
    private Integer screenOrder;

    @Schema(description = "筛查时间")
    @ExcelProperty("筛查时间")
    private LocalDateTime screenTime;

    @Schema(description = "工作年度")
    private Integer year;

    @Schema(description = "筛查类型")
    private Integer screenType;

    /**
     * 对应摸底表中患者姓名
     */
    private String name;

    private String idNum;
    private String padId;

}