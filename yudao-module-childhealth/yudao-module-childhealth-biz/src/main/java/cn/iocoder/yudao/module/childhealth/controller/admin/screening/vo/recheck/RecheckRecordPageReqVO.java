package cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.recheck;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

@Schema(description = "管理后台 - 复筛记录分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RecheckRecordPageReqVO extends PageParam {

    @Schema(description = "关联阳性记录ID", example = "1")
    private Long positiveId;

    @Schema(description = "学生ID", example = "1")
    private Long studentId;

    @Schema(description = "复筛日期")
    private LocalDate[] recheckDate;

    @Schema(description = "是否仍为阳性 0否 1是", example = "0")
    private Integer isStillPositive;

}