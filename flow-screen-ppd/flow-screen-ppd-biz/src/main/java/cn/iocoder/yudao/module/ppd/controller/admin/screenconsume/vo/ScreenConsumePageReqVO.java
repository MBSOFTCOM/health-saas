package cn.iocoder.yudao.module.ppd.controller.admin.screenconsume.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import java.util.List;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;


@Schema(description = "管理后台 - 消耗管理分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ScreenConsumePageReqVO extends PageParam {

    @Schema(description = "试剂id")
    private Long reagentId;

    @Schema(description = "试剂名称")
    private String reagentName;

    @Schema(description = "试剂类型")
    private Integer reagentType;

    @Schema(description = "消耗序位")
    private Integer consumeOrder;

    @Schema(description = "批次号")
    private String bathNumber;

    @Schema(description = "入库量（按试剂）")
    private Integer inboundNumber;

    @Schema(description = "当前库存")
    private Integer currentNumber;

    @Schema(description = "生产日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] manufactureDate;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "转换系数（人次）")
    private Integer reagentSpecsNum;

    @Schema(description = "库存预警值（按试剂）")
    private Integer threshold;

    @Schema(description = "是否启用")
    private Integer usable;

    @Schema(description = "有效期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private String[] indate;

    @Schema(description = "部门列表")
    private Long deptList;

    @Schema(description = "部门id")
    private Long deptId;


}