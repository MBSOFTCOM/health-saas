package cn.iocoder.yudao.module.ppd.controller.admin.screenpoint.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 筛查点新增/修改 Request VO")
@Data
public class ScreenPointSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "筛查点名称")
    private String name;

    @Schema(description = "队长")
    private String worker;

    @Schema(description = "采集组工作人员")
    private String collectWorker;

    @Schema(description = "PPD组工作人员")
    private String ppdWorker;

    @Schema(description = "DR/CT组工组人员")
    private String drctWorker;

    @Schema(description = "痰检组工作人员")
    private String sputumWorker;

    @Schema(description = "实验组工作人员")
    private String experimentWorker;

    @Schema(description = "心电图组工作人员")
    private String electrocardiogramWorker;

    @Schema(description = "诊断组工作人员")
    private String diagnosisWorker;

    @Schema(description = "筛查单位")
    private String screenDept;

    @Schema(description = "工作年度")
    private Integer year;
}