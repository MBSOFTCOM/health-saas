package cn.iocoder.yudao.module.childhealth.controller.admin.childbase.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 监护人信息创建请求 VO
 *
 * @author 系统
 */
@Schema(description = "管理后台 - 监护人信息创建 Request VO")
@Data
public class GuardianInfoCreateReqVO {

    @Schema(description = "关系：1-父亲，2-母亲，3-其他", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "监护人关系不能为空")
    private Integer relation;

    @Schema(description = "监护人姓名", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @NotBlank(message = "监护人姓名不能为空")
    @Size(max = 50, message = "监护人姓名长度不能超过50个字符")
    private String name;

    @Schema(description = "监护人电话", requiredMode = Schema.RequiredMode.REQUIRED, example = "13800138000")
    @NotBlank(message = "监护人电话不能为空")
    @Size(max = 11, message = "监护人电话长度不能超过11个字符")
    private String mobile;

    @Schema(description = "监护人身份证号", example = "640100199001010001")
    @Size(max = 18, message = "身份证号长度不能超过18个字符")
    private String idCard;

    @Schema(description = "是否主要监护人：0-否，1-是", example = "1")
    private Integer isPrimary;

    @Schema(description = "微信OpenID", example = "oXXXXXXXXXXXXXXXXXXXXXXXXX")
    private String wechatOpenid;

}