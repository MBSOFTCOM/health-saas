package cn.iocoder.yudao.module.childhealth.controller.admin.childbase.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 儿童档案分页查询请求 VO
 *
 * @author 系统
 */
@Schema(description = "管理后台 - 儿童档案分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class ChildBaseInfoPageReqVO extends PageParam {

    @Schema(description = "儿童编码", example = "CHILD-20240101-001")
    private String childCode;

    @Schema(description = "姓名，模糊匹配", example = "张小明")
    private String name;

    @Schema(description = "性别：1-男，2-女", example = "1")
    private Integer gender;

    @Schema(description = "身份证号", example = "640100202001010001")
    private String idCard;

    @Schema(description = "是否高危儿：0-否，1-是", example = "1")
    private Integer isHighRisk;

    @Schema(description = "状态：1-正常，2-转出，3-死亡", example = "1")
    private Integer status;

    @Schema(description = "出生日期-开始", example = "2020-01-01")
    private LocalDate birthDateStart;

    @Schema(description = "出生日期-结束", example = "2023-12-31")
    private LocalDate birthDateEnd;

    @Schema(description = "监护人姓名，模糊匹配", example = "张三")
    private String guardianName;

    @Schema(description = "监护人电话", example = "13800138000")
    private String guardianPhone;

}