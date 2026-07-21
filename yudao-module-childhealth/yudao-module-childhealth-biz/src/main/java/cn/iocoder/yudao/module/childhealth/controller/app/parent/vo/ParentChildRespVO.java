package cn.iocoder.yudao.module.childhealth.controller.app.parent.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * 家长 App - 我的孩子列表项 Response VO
 *
 * 创建日期: 2026-07-20
 * 模块: 移动端 - 家长 App
 */
@Schema(description = "家长 App - 我的孩子列表项 VO")
@Data
public class ParentChildRespVO {

    @Schema(description = "学生ID", example = "1001")
    private Long studentId;

    @Schema(description = "儿童档案ID", example = "2001")
    private Long childId;

    @Schema(description = "学生姓名", example = "张小明")
    private String studentName;

    @Schema(description = "性别 1男 2女", example = "1")
    private Integer gender;

    @Schema(description = "出生日期", example = "2018-05-12")
    private LocalDate birthDate;

    @Schema(description = "学校ID", example = "10")
    private Long schoolId;

    @Schema(description = "学校名称", example = "市第一小学")
    private String schoolName;

    @Schema(description = "班级ID", example = "100")
    private Long classId;

    @Schema(description = "班级名称", example = "二年级3班")
    private String className;

    @Schema(description = "最近一次筛查日期", example = "2026-03-15")
    private LocalDate lastScreeningDate;

    @Schema(description = "是否有未读阳性通知 0否 1是", example = "1")
    private Integer hasUnreadPositive;

}
