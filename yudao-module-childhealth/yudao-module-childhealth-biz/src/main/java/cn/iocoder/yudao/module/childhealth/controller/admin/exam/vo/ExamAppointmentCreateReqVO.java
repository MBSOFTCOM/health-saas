package cn.iocoder.yudao.module.childhealth.controller.admin.exam.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 体检预约创建请求 VO
 */
@Schema(description = "体检预约创建请求")
@Data
public class ExamAppointmentCreateReqVO {

    @Schema(description = "儿童ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long childId;

    @Schema(description = "体检类型 1常规 2入园 3专项", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer examType;

    @Schema(description = "体检月龄")
    private Integer examMonthAge;

    @Schema(description = "预约日期", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate appointmentDate;

    @Schema(description = "预约时间")
    private LocalTime appointmentTime;

    @Schema(description = "科室ID")
    private Long deptId;

    @Schema(description = "医生ID")
    private Long doctorId;

    @Schema(description = "来源 1线下 2线上 3系统自动")
    private Integer source;
}