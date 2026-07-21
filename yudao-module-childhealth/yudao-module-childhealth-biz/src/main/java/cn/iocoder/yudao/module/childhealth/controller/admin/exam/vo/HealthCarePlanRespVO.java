package cn.iocoder.yudao.module.childhealth.controller.admin.exam.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 公卫儿童保健计划 Response VO")
@Data
public class HealthCarePlanRespVO {

    @Schema(description = "计划ID")
    private Long id;

    @Schema(description = "儿童ID")
    private Long childId;

    @Schema(description = "计划类型 1常规公卫 2入园 3专项")
    private Integer planType;

    @Schema(description = "计划开始日期")
    private LocalDate startDate;

    @Schema(description = "计划结束日期")
    private LocalDate endDate;

    @Schema(description = "总节点数")
    private Integer totalNodes;

    @Schema(description = "已完成节点数")
    private Integer completedNodes;

    @Schema(description = "状态 1进行中 2已完成 3已终止")
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
