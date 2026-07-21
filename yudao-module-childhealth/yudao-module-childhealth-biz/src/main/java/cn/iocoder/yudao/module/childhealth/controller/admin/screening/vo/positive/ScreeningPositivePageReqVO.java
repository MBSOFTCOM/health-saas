package cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.positive;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 筛查阳性记录分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ScreeningPositivePageReqVO extends PageParam {

    @Schema(description = "筛查记录ID", example = "1")
    private Long recordId;

    @Schema(description = "学生ID", example = "1")
    private Long studentId;

    @Schema(description = "疾病编码", example = "J00.0")
    private String diseaseCode;

    @Schema(description = "疾病名称", example = "近视")
    private String diseaseName;

    @Schema(description = "阳性等级 1轻度 2中度 3重度", example = "1")
    private Integer positiveLevel;

    @Schema(description = "是否需要复筛 0否 1是", example = "1")
    private Integer needRecheck;

    @Schema(description = "复筛状态 0未通知 1通知已入队 2已完成", example = "0")
    private Integer recheckStatus;

}