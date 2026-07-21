package cn.iocoder.yudao.module.childhealth.controller.admin.ops.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 工作量统计新增/修改 Request VO
 *
 * 创建日期: 2026-07-20
 */
@Schema(description = "管理后台 - 工作量统计新增/修改 Request VO")
@Data
public class StaffWorkloadStatisticsSaveReqVO {

    @Schema(description = "主键ID", example = "1")
    private Long id;

    @Schema(description = "统计日期", requiredMode = Schema.RequiredMode.REQUIRED, example = "2026-07-20")
    private LocalDate statDate;

    @Schema(description = "医护ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    private Long staffId;

    @Schema(description = "医护姓名", example = "李医生")
    private String staffName;

    @Schema(description = "科室ID", example = "10")
    private Long deptId;

    @Schema(description = "机构ID", example = "1")
    private Long orgId;

    @Schema(description = "机构名称", example = "市儿童医院")
    private String orgName;

    @Schema(description = "关联学校ID", example = "100")
    private Long schoolId;

    @Schema(description = "筛查数", example = "50")
    private Integer screeningCount;

    @Schema(description = "审核数", example = "30")
    private Integer auditCount;

    @Schema(description = "随访数", example = "20")
    private Integer followCount;

    @Schema(description = "复筛数", example = "10")
    private Integer recheckCount;

    @Schema(description = "转介数", example = "5")
    private Integer referralCount;

    @Schema(description = "专案创建数", example = "3")
    private Integer caseCreateCount;

    @Schema(description = "专案结案数", example = "1")
    private Integer caseCloseCount;

    @Schema(description = "总工作量(加权得分)", example = "85.50")
    private BigDecimal totalWorkload;

    @Schema(description = "扩展指标JSON")
    private String extraMetrics;

}
