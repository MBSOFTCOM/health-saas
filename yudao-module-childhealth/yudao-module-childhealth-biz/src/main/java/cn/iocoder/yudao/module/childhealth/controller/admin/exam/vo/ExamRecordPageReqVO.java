package cn.iocoder.yudao.module.childhealth.controller.admin.exam.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 体检记录分页请求 VO
 */
@Schema(description = "体检记录分页请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class ExamRecordPageReqVO extends PageParam {

    @Schema(description = "儿童ID")
    private Long childId;

    @Schema(description = "体检类型 1常规 2入园 3专项")
    private Integer examType;

    @Schema(description = "体检日期")
    private LocalDate examDate;

    @Schema(description = "检查状态 1进行中 2待审核 3已完成")
    private Integer checkStatus;

    @Schema(description = "是否有异常")
    private Boolean hasAbnormal;
}