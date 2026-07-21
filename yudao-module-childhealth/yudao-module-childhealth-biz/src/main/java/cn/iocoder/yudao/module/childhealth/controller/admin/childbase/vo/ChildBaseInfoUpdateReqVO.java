package cn.iocoder.yudao.module.childhealth.controller.admin.childbase.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 儿童档案更新请求 VO
 *
 * @author 系统
 */
@Schema(description = "管理后台 - 儿童档案更新 Request VO")
@Data
public class ChildBaseInfoUpdateReqVO {

    @Schema(description = "儿童ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "儿童ID不能为空")
    private Long id;

    @Schema(description = "姓名", example = "张小明")
    @Size(max = 50, message = "姓名长度不能超过50个字符")
    private String name;

    @Schema(description = "性别：1-男，2-女", example = "1")
    private Integer gender;

    @Schema(description = "出生日期", example = "2020-01-01")
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

    @Schema(description = "状态：1-正常，2-转出，3-死亡", example = "1")
    private Integer status;

}