package cn.iocoder.yudao.module.ppd.controller.admin.screenstaticshistory.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 工作进展报告-统计表-导出的历史选项分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ScreenStaticsHistoryPageReqVO extends PageParam {

    @Schema(description = "机构id", example = "31615")
    private Long deptId;

    @Schema(description = "表格标题")
    private String tableTittle;

    @Schema(description = "学校名称")
    private String school;

    @Schema(description = "医院名称")
    private String hospital;

    @Schema(description = "行政区划名称")
    private String district;

    @Schema(description = "联系人姓名")
    private String contact;

    @Schema(description = "联系电话")
    private String contactPhone;

    @Schema(description = "注射人姓名")
    private String injectionPeople;

    @Schema(description = "查验人姓名")
    private String checkPeople;

    @Schema(description = "基本信息勾选，如'[1,2,3,4]'")
    private String infoList;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}