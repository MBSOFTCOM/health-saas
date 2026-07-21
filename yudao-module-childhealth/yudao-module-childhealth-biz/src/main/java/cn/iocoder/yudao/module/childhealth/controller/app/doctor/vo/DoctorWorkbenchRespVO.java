package cn.iocoder.yudao.module.childhealth.controller.app.doctor.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 医生 App - 医生工作台 VO
 *
 * 创建日期: 2026-07-20
 * 模块: 移动端 - 医生 App
 */
@Schema(description = "医生 App - 医生工作台 VO")
@Data
public class DoctorWorkbenchRespVO {

    @Schema(description = "医生ID", example = "100")
    private Long doctorId;

    @Schema(description = "医生姓名", example = "李医生")
    private String doctorName;

    @Schema(description = "今日待筛查数", example = "50")
    private Integer todayPendingScreening;

    @Schema(description = "今日已筛查数", example = "30")
    private Integer todayScreened;

    @Schema(description = "今日待审核数", example = "20")
    private Integer todayPendingAudit;

    @Schema(description = "今日已审核数", example = "10")
    private Integer todayAudited;

    @Schema(description = "待随访任务数", example = "15")
    private Integer pendingFollowCount;

    @Schema(description = "待复筛任务数", example = "8")
    private Integer pendingRecheckCount;

    @Schema(description = "本月总工作量", example = "256.50")
    private String monthTotalWorkload;

    @Schema(description = "统计日期", example = "2026-07-20")
    private LocalDate statDate;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

}
