package cn.iocoder.yudao.module.childhealth.controller.admin.ops.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * 工作量统计分页 Request VO
 *
 * 创建日期: 2026-07-20
 */
@Schema(description = "管理后台 - 工作量统计分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StaffWorkloadStatisticsPageReqVO extends PageParam {

    @Schema(description = "开始统计日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate statDateStart;

    @Schema(description = "结束统计日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate statDateEnd;

    @Schema(description = "医护ID", example = "100")
    private Long staffId;

    @Schema(description = "科室ID", example = "10")
    private Long deptId;

    @Schema(description = "机构ID", example = "1")
    private Long orgId;

    @Schema(description = "学校ID", example = "100")
    private Long schoolId;

}
