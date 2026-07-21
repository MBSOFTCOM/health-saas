package cn.iocoder.yudao.module.childhealth.controller.admin.childbase.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 监护人信息响应 VO
 *
 * @author 系统
 */
@Schema(description = "管理后台 - 监护人信息 Response VO")
@Data
public class GuardianInfoRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "儿童ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long childId;

    @Schema(description = "关系：1-父亲，2-母亲，3-其他", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer relation;

    @Schema(description = "监护人姓名", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    private String name;

    @Schema(description = "监护人电话", requiredMode = Schema.RequiredMode.REQUIRED, example = "13800138000")
    private String mobile;

    @Schema(description = "监护人身份证号", example = "640100199001010001")
    private String idCard;

    @Schema(description = "是否主要监护人：0-否，1-是", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer isPrimary;

    @Schema(description = "微信OpenID", example = "oXXXXXXXXXXXXXXXXXXXXXXXXX")
    private String wechatOpenid;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}