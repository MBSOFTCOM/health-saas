package cn.iocoder.yudao.module.childhealth.controller.admin.medical.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - LIS/PACS检验检查报告分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LisPacsReportPageReqVO extends PageParam {

    @Schema(description = "儿童档案ID", example = "1001")
    private Long childId;

    @Schema(description = "报告单号", example = "LIS20260720001")
    private String reportNo;

    @Schema(description = "报告类型 LIS/PACS", example = "LIS")
    private String reportType;

    @Schema(description = "来源系统（HIS系统名/设备名）", example = "HIS-MAIN")
    private String sourceSystem;

    @Schema(description = "检查项目", example = "血常规")
    private String examItem;

    @Schema(description = "状态 0待处理 1已归档到病历 2已忽略", example = "0")
    private Integer status;

    @Schema(description = "关联病历ID", example = "100")
    private Long medicalRecordId;

}
