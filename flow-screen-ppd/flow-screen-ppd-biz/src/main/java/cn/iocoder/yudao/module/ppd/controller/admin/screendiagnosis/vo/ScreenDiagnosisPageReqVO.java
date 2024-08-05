package cn.iocoder.yudao.module.ppd.controller.admin.screendiagnosis.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 诊断组分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ScreenDiagnosisPageReqVO extends PageParam {

    @Schema(description = "筛查编号", example = "4179")
    private String screenId;

    @Schema(description = "同步时唯一编码", example = "1651")
    private Long syncId;

    @Schema(description = "医生签名")
    private String doctorSignature;

    @Schema(description = "筛查时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] screenTime;

    @Schema(description = "是否网报 0-否 1-是")
    private Boolean report;

    @Schema(description = "符合潜伏治疗条件者是否进行预防性治疗 0-否 1-是")
    private String preventiveTreatment;

    @Schema(description = "筛查次序")
    private Integer screenOrder;

    @Schema(description = "对应摸底表中id", example = "3081")
    private Long personId;

    @Schema(description = "筛查点")
    private String screenPoint;

    @Schema(description = "诊断结果：1-疑似肺结核 2-肺结核 3-肺外结核、4-其他")
    private Integer outcome;

    @Schema(description = "治疗方案: 1=门诊治疗、2=住院治疗、3=门诊+住院治疗")
    private Integer treatmentProgram;

    @Schema(description = "备注", example = "你猜")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;



    @Schema(description = "身份证号")
    private String idNum;

    @Schema(description = "姓名")
    private String name;

    /**
     * 年份
     */
    private String year;

    /**
     * 筛查类型
     */
    private String screenType;

}