package cn.iocoder.yudao.module.childhealth.controller.admin.exam.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 体检预约分页请求 VO
 */
@Schema(description = "体检预约分页请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class ExamAppointmentPageReqVO extends PageParam {

    @Schema(description = "儿童ID")
    private Long childId;

    @Schema(description = "体检类型 1常规 2入园 3专项")
    private Integer examType;

    @Schema(description = "预约日期")
    private LocalDate appointmentDate;

    @Schema(description = "状态 1已预约 2已到检 3已完成 4已取消")
    private Integer status;
}