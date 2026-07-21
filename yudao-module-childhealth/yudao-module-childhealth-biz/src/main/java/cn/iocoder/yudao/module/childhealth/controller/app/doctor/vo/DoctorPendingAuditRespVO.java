package cn.iocoder.yudao.module.childhealth.controller.app.doctor.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 医生 App - 待审核筛查记录 VO
 *
 * 创建日期: 2026-07-20
 * 模块: 移动端 - 医生 App
 */
@Schema(description = "医生 App - 待审核筛查记录 VO")
@Data
public class DoctorPendingAuditRespVO {

    @Schema(description = "筛查记录ID", example = "5001")
    private Long recordId;

    @Schema(description = "记录流水号", example = "SR20260315001")
    private String recordNo;

    @Schema(description = "学生ID", example = "1001")
    private Long studentId;

    @Schema(description = "学生姓名", example = "张小明")
    private String studentName;

    @Schema(description = "批次ID", example = "100")
    private Long batchId;

    @Schema(description = "批次名称", example = "2026年春季视力筛查")
    private String batchName;

    @Schema(description = "筛查日期")
    private String screeningDate;

    @Schema(description = "审核状态 1进行中 2待审核 3已审核", example = "2")
    private Integer checkStatus;

    @Schema(description = "是否阳性 0否 1是", example = "1")
    private Integer hasPositive;

    @Schema(description = "阳性项目JSON")
    private String positiveItems;

}
