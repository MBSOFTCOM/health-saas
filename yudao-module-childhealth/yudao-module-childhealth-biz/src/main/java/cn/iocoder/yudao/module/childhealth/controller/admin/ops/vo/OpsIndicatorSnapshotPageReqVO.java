package cn.iocoder.yudao.module.childhealth.controller.admin.ops.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

/**
 * 运营指标快照分页 Request VO
 *
 * 创建日期: 2026-07-20
 */
@Schema(description = "管理后台 - 运营指标快照分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OpsIndicatorSnapshotPageReqVO extends PageParam {

    @Schema(description = "开始快照日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate snapshotDateStart;

    @Schema(description = "结束快照日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate snapshotDateEnd;

    @Schema(description = "批次ID", example = "100")
    private Long batchId;

    @Schema(description = "学校ID", example = "10")
    private Long schoolId;

    @Schema(description = "年级ID", example = "100")
    private Long gradeId;

    @Schema(description = "行政区划代码", example = "440100")
    private String regionCode;

}
