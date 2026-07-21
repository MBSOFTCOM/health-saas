package cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.record;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 筛查记录列表 Request VO")
@Data
public class ScreeningRecordListReqVO {

    @Schema(description = "批次ID", example = "1")
    private Long batchId;

    @Schema(description = "学生ID", example = "1")
    private Long studentId;

    @Schema(description = "审核状态 1进行中 2待审核 3已审核", example = "1")
    private Integer checkStatus;

    @Schema(description = "是否有阳性 0否 1是", example = "0")
    private Integer hasPositive;

}