package cn.iocoder.yudao.module.ppd.controller.admin.synchronization.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScreenPointAndMemberVO {

    @Schema(description = "筛查点名称")
    private String name;

    @Schema(description = "筛查单位")
    private String agency;

    @Schema(description = "队长")
    private String worker;

    @Schema(description = "采集组")
    private String collectWorker;

    @Schema(description = "ppd组")
    private String ppdWorker;

    @Schema(description = "胸片组")
    private String drctWorker;

    @Schema(description = "痰检组")
    private String sputumWorker;

    @Schema(description = "心电组")
    private String electrocardiogramWorker;

    @Schema(description = "实验组")
    private String experimentWorker;

    @Schema(description = "诊断组")
    private String diagnosisWorker;

    @Schema(description = "工作年度")
    private Integer year;

}
