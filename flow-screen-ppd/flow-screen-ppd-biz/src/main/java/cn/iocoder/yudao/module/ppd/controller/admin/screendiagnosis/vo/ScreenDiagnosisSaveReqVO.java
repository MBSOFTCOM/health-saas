package cn.iocoder.yudao.module.ppd.controller.admin.screendiagnosis.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 诊断组新增/修改 Request VO")
@Data
public class ScreenDiagnosisSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "10317")
    private Long id;

    @Schema(description = "筛查编号", example = "4179")
    private String screenId;

    @Schema(description = "同步时唯一编码", example = "1651")
    private Long syncId;

    @Schema(description = "医生签名")
    private String doctorSignature;

    @Schema(description = "筛查时间")
    private LocalDateTime screenTime;

    @Schema(description = "是否网报 0-否 1-是")
    private Integer report;

    @Schema(description = "符合潜伏治疗条件者是否进行预防性治疗 0-否 1-是")
    private Integer preventiveTreatment;

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

    /**
     * 筛查类型
     */
    private Integer screenType;
    /**
     * 筛查年份
     */
    private Integer year;
}