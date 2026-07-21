package cn.iocoder.yudao.module.childhealth.controller.admin.childbase.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 儿童档案创建请求 VO
 *
 * @author 系统
 */
@Schema(description = "管理后台 - 儿童档案创建 Request VO")
@Data
public class ChildBaseInfoCreateReqVO {

    @Schema(description = "儿童唯一编码，如果不传则自动生成", example = "CHILD-20240101-001")
    private String childCode;

    @Schema(description = "姓名", requiredMode = Schema.RequiredMode.REQUIRED, example = "张小明")
    @NotBlank(message = "姓名不能为空")
    @Size(max = 50, message = "姓名长度不能超过50个字符")
    private String name;

    @Schema(description = "性别：1-男，2-女", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "性别不能为空")
    private Integer gender;

    @Schema(description = "出生日期", requiredMode = Schema.RequiredMode.REQUIRED, example = "2020-01-01")
    @NotNull(message = "出生日期不能为空")
    private LocalDate birthDate;

    @Schema(description = "身份证号", example = "640100202001010001")
    @Size(max = 18, message = "身份证号长度不能超过18个字符")
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

    @Schema(description = "高危标签，JSON数组格式", example = "[\"早产\", \"低体重\"]")
    private String highRiskTags;

    @Schema(description = "监护人信息列表")
    @Valid
    private List<GuardianInfoCreateReqVO> guardians;

}