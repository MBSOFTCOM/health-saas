package cn.iocoder.yudao.module.ppd.controller.admin.screenpoint.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 筛查点 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ScreenPointRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "筛查点名称")
    @ExcelProperty("筛查点名称")
    private String name;

    @Schema(description = "筛查单位")
    @ExcelProperty("筛查单位")
    private String screenDept;

    @Schema(description = "工作年度")
    @ExcelProperty("工作年度")
    private Integer year;

    @Schema(description = "队长")
    @ExcelProperty("队长")
    private String worker;

    @Schema(description = "采集组工作人员")
    @ExcelProperty("采集组工作人员")
    private String collectWorker;

    @Schema(description = "PPD组工作人员")
    @ExcelProperty("PDD组工作人员")
    private String ppdWorker;

    @Schema(description = "DR/CT组工组人员")
    @ExcelProperty("DR/CT组工组人员")
    private String drctWorker;

    @Schema(description = "痰检组工作人员")
    @ExcelProperty("痰检组工作人员")
    private String sputumWorker;

    @Schema(description = "实验组工作人员")
    @ExcelProperty("实验组工作人员")
    private String experimentWorker;

    @Schema(description = "心电图组工作人员")
    @ExcelProperty("心电图组工作人员")
    private String electrocardiogramWorker;

    @Schema(description = "诊断组工作人员")
    @ExcelProperty("诊断组工作人员")
    private String diagnosisWorker;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}