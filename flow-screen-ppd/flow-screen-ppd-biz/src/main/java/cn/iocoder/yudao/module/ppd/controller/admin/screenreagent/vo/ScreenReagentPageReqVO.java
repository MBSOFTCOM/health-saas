package cn.iocoder.yudao.module.ppd.controller.admin.screenreagent.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 试剂分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ScreenReagentPageReqVO extends PageParam {

    @Schema(description = "试剂名称")
    private String name;

    @Schema(description = "试剂类型")
    private Integer type;

    @Schema(description = "转换系数（人次）")
    private Integer reagentSpecsNum;

    @Schema(description = "是否启用")
    private Integer usable;

    @Schema(description = "效价")
    private BigDecimal titer;

    @Schema(description = "效价单位")
    private Integer potencyUnit;

    @Schema(description = "规格")
    private BigDecimal specification;

    @Schema(description = "规格单位")
    private Integer specificationUnit;

    @Schema(description = "包装单位")
    private Integer packageUnit;

    @Schema(description = "供应商")
    private String manufacturer;

    @Schema(description = "库存预警值（按试剂）")
    private Integer threshold;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "部门列表")
    private Long deptList;

}