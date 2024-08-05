package cn.iocoder.yudao.module.ppd.controller.admin.screenpoint.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 筛查点分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ScreenPointPageReqVO extends PageParam {

    @Schema(description = "筛查点名称")
    private String name;

    @Schema(description = "筛查单位")
    private String screenDept;

    @Schema(description = "工作年度")
    private Integer year;

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


}