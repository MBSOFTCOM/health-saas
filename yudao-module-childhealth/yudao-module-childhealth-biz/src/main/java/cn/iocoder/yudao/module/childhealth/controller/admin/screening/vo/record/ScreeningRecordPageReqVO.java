package cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.record;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

@Schema(description = "管理后台 - 筛查记录分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ScreeningRecordPageReqVO extends PageParam {

    @Schema(description = "筛查流水号", example = "SCR2024001")
    private String recordNo;

    @Schema(description = "批次ID", example = "1")
    private Long batchId;

    @Schema(description = "学生ID", example = "1")
    private Long studentId;

    @Schema(description = "筛查日期")
    private LocalDate[] screeningDate;

    @Schema(description = "审核状态 1进行中 2待审核 3已审核", example = "1")
    private Integer checkStatus;

    @Schema(description = "是否有阳性 0否 1是", example = "0")
    private Integer hasPositive;

}