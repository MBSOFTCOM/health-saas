package cn.iocoder.yudao.module.childhealth.controller.admin.childbase.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 儿童档案响应 VO
 *
 * @author 系统
 */
@Schema(description = "管理后台 - 儿童档案信息 Response VO")
@Data
public class ChildBaseInfoRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "儿童唯一编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "CHILD-20240101-001")
    private String childCode;

    @Schema(description = "姓名", requiredMode = Schema.RequiredMode.REQUIRED, example = "张小明")
    private String name;

    @Schema(description = "性别：1-男，2-女", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer gender;

    @Schema(description = "出生日期", requiredMode = Schema.RequiredMode.REQUIRED, example = "2020-01-01")
    private LocalDate birthDate;

    @Schema(description = "身份证号", example = "640100202001010001")
    private String idCard;

    @Schema(description = "出生体重(kg)", example = "3.5")
    private BigDecimal birthWeight;

    @Schema(description = "出生身长(cm)", example = "50.0")
    private BigDecimal birthHeight;

    @Schema(description = "胎龄(周)", example = "40")
    private Integer gestationalAge;

    @Schema(description = "是否早产：0-否，1-是", example = "0")
    private Integer isPremature;

    @Schema(description = "是否高危儿：0-否，1-是", example = "0")
    private Integer isHighRisk;

    @Schema(description = "高危标签", example = "DEL_PRETERM,DEL_LOW_WEIGHT")
    private String highRiskTags;

    @Schema(description = "高危管理等级：1-一般，2-重点关注，3-严密管理", example = "1")
    private Integer highRiskLevel;

    @Schema(description = "建档来源：1-手动，2-微信自助，3-孕保拉取，4-HIS自动", example = "1")
    private Integer registerSource;

    @Schema(description = "首次就诊日期")
    private LocalDate firstVisitDate;

    @Schema(description = "二维码URL", example = "http://example.com/qr/child_001.png")
    private String qrCode;

    @Schema(description = "状态：1-正常，2-转出，3-死亡", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "监护人信息列表")
    private List<GuardianInfoRespVO> guardians;

}