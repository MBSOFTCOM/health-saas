package cn.iocoder.yudao.module.ppd.controller.admin.screensum.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 汇总 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ScreenSumRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "31")
    @ExcelProperty("主键")
    private Long id;

    @Schema(description = "筛查年份")
    @ExcelProperty("筛查年份")
    private String year;

    @Schema(description = "筛查编号", example = "14440")
    @ExcelProperty("筛查编号")
    private String screenId;

    @Schema(description = "对应摸底表中id", example = "4623")
    @ExcelProperty("对应摸底表中id")
    private Long personId;

    @Schema(description = "身份证")
    @ExcelProperty("身份证")
    private String idNum;

    @Schema(description = "同步时唯一编码", example = "8496")
    @ExcelProperty("同步时唯一编码")
    private Long syncId;

    @Schema(description = "采集表id", example = "2644")
    @ExcelProperty("采集表id")
    private Long collectId;

    @Schema(description = "ppd表id", example = "4570")
    @ExcelProperty("ppd表id")
    private Long ppdId;

    @Schema(description = "dr胸片表id", example = "10663")
    @ExcelProperty("dr胸片表id")
    private Long chestRadiographId;


    @Schema(description = "筛查类型")
    private Integer screenType;

    @Schema(description = "当前已完成的分组")
    private String curFinish;

    /**
     * ct表id
     */
    private Long computedTomographyId;

}