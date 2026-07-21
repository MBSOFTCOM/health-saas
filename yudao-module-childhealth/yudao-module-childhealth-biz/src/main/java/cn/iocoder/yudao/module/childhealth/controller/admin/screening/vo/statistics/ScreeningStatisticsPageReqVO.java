package cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.statistics;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

@Schema(description = "管理后台 - 筛查统计分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ScreeningStatisticsPageReqVO extends PageParam {

    @Schema(description = "统计日期-开始")
    private LocalDate[] statDate;

    @Schema(description = "批次ID", example = "1")
    private Long batchId;

    @Schema(description = "学校ID", example = "1")
    private Long schoolId;

    @Schema(description = "年级ID", example = "1")
    private Long gradeId;

}