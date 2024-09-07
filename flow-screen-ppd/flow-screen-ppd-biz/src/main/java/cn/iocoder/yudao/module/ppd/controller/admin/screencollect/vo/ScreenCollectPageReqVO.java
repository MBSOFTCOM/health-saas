package cn.iocoder.yudao.module.ppd.controller.admin.screencollect.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 采集分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ScreenCollectPageReqVO extends PageParam {

    @Schema(description = "筛查编号", example = "10874")
    private String screenId;

    @Schema(description = "同步时唯一编码", example = "13599")
    private Long syncId;

    @Schema(description = "医生签名")
    private String doctorSignature;

    @Schema(description = "筛查单位")
    private String screenAgency;

    @Schema(description = "筛查时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] screenTime;

    @Schema(description = "筛查次序")
    private Integer screenOrder;

    @Schema(description = "对应摸底表中id", example = "19490")
    private Long personId;

    @Schema(description = "结果（分新生筛查和其他）。	新生：1-咳嗽、咳痰不小于2周 2-痰中带血或咯血 3-反复发热2周以上 4-淋巴结肿大	其他：1-咳嗽、咳痰（超过一周）2-血痰或咯血 3-发热	4--胸痛 5-夜间盗汗 6-食欲不振 7-乏力 8-体重减轻（超过6斤 9-卡痕异常	")
    private String outcome;

    @Schema(description = "筛查点")
    private String screenPoint;

    @Schema(description = "年份")
    private Integer year;

    @Schema(description = "筛查类型")
    private Integer screenType;

    @Schema(description = "数据类型 0-非小程序 1-小程序")
    private Integer dataSource;
}