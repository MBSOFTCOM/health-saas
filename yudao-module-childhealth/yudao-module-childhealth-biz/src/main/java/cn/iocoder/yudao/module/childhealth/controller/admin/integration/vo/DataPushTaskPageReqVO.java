package cn.iocoder.yudao.module.childhealth.controller.admin.integration.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

/**
 * 数据上报任务分页 Request VO
 *
 * 创建日期: 2026-07-20
 */
@Schema(description = "管理后台 - 数据上报任务分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DataPushTaskPageReqVO extends PageParam {

    @Schema(description = "任务编号", example = "PT20260720001")
    private String taskNo;

    @Schema(description = "目标系统 HIS/LIS/WCHB/WSJW/EDU", example = "HIS")
    private String targetSystem;

    @Schema(description = "业务类型 SCREENING/REFERRAL/FOLLOW/RECHECK", example = "SCREENING")
    private String businessType;

    @Schema(description = "推送状态 0待推送 1推送中 2成功 3失败 4部分成功 5已取消", example = "0")
    private Integer pushStatus;

    @Schema(description = "关联批次ID", example = "100")
    private Long batchId;

    @Schema(description = "推送模式 1手动 2自动", example = "1")
    private Integer pushMode;

    @Schema(description = "开始创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime createTimeStart;

    @Schema(description = "结束创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime createTimeEnd;

}
