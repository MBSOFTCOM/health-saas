package cn.iocoder.yudao.module.ppd.controller.admin.screeninformedconsentform.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 知情同意书 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ScreenInformedConsentFormRespVO {

    @Schema(description = "自增id", requiredMode = Schema.RequiredMode.REQUIRED, example = "10642")
    @ExcelProperty("自增id")
    private Long id;

    @Schema(description = "受筛查学生的id（待筛查人员id）", example = "20072")
    @ExcelProperty("受筛查学生的id（待筛查人员id）")
    private Long studentId;

    @Schema(description = "受筛查学生的身份证", example = "20072")
    @ExcelProperty("受筛查学生的身份证")
    private String idNum;

    @Schema(description = "受筛查学生的姓名", example = "张三")
    @ExcelProperty("受筛查学生的姓名")
    private String name;

    @Schema(description = "学校", example = "赵六")
    @ExcelProperty("学校")
    private String schoolName;

    @Schema(description = "班级")
    @ExcelProperty("班级")
    private String classroom;

    @Schema(description = "拒绝签署原因", example = "不对")
    @ExcelProperty("拒绝签署原因")
    private String reason;

    @Schema(description = "家长签名图片地址")
    @ExcelProperty("家长签名图片地址")
    private String signature;

    @Schema(description = "是否签署1：是  2：否")
    private Integer isSign;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}