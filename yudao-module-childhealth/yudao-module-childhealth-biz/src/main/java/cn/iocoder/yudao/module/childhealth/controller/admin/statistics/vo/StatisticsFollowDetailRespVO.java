package cn.iocoder.yudao.module.childhealth.controller.admin.statistics.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * 随访明细统计 Response VO
 *
 * 创建日期: 2026-07-20
 * 模块: 20. 全维度数据统计
 * 用途: 随访内容/健康变化/干预意见明细
 */
@Schema(description = "管理后台 - 随访明细统计 Response VO")
@Data
public class StatisticsFollowDetailRespVO {

    @Schema(description = "随访任务ID", example = "1001")
    private Long followTaskId;

    @Schema(description = "学生ID", example = "1001")
    private Long studentId;

    @Schema(description = "学生姓名", example = "张三")
    private String studentName;

    @Schema(description = "项目编码", example = "VISION")
    private String itemCode;

    @Schema(description = "项目名称", example = "视力")
    private String itemName;

    @Schema(description = "随访日期", example = "2026-07-20")
    private LocalDate followDate;

    @Schema(description = "随访内容")
    private String followContent;

    @Schema(description = "健康变化 1改善 2稳定 3恶化", example = "1")
    private Integer healthChange;

    @Schema(description = "干预意见")
    private String interventionAdvice;

    @Schema(description = "随访状态 0待随访 1已完成 2已逾期", example = "1")
    private Integer followStatus;

    @Schema(description = "医生ID", example = "100")
    private Long doctorId;

    @Schema(description = "医生姓名", example = "李医生")
    private String doctorName;

}
