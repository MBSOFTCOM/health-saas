package cn.iocoder.yudao.module.childhealth.controller.admin.exam.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 体检预约响应 VO
 */
@Schema(description = "体检预约响应")
@Data
public class ExamAppointmentRespVO {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "儿童ID")
    private Long childId;

    @Schema(description = "体检类型 1常规 2入园 3专项")
    private Integer examType;

    @Schema(description = "体检月龄")
    private Integer examMonthAge;

    @Schema(description = "预约日期")
    private LocalDate appointmentDate;

    @Schema(description = "预约时间")
    private LocalTime appointmentTime;

    @Schema(description = "科室ID")
    private Long deptId;

    @Schema(description = "医生ID")
    private Long doctorId;

    @Schema(description = "状态 1已预约 2已到检 3已完成 4已取消")
    private Integer status;

    @Schema(description = "来源 1线下 2线上 3系统自动")
    private Integer source;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}