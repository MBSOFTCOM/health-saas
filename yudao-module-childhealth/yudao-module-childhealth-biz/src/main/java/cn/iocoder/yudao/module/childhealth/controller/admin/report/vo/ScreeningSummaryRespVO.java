package cn.iocoder.yudao.module.childhealth.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 筛查总表响应VO
 */
@Schema(description = "筛查总表响应")
@Data
public class ScreeningSummaryRespVO {

    @Schema(description = "记录ID")
    private Long recordId;

    @Schema(description = "记录编号")
    private String recordNo;

    @Schema(description = "批次ID")
    private Long batchId;

    @Schema(description = "批次名称")
    private String batchName;

    @Schema(description = "区域编码")
    private String regionCode;

    @Schema(description = "区域名称")
    private String regionName;

    @Schema(description = "学校ID")
    private Long schoolId;

    @Schema(description = "学校名称")
    private String schoolName;

    @Schema(description = "年级ID")
    private Long gradeId;

    @Schema(description = "年级名称")
    private String gradeName;

    @Schema(description = "班级ID")
    private Long classId;

    @Schema(description = "班级名称")
    private String className;

    @Schema(description = "学生ID")
    private Long studentId;

    @Schema(description = "学生姓名")
    private String studentName;

    @Schema(description = "性别：1-男，2-女")
    private Integer gender;

    @Schema(description = "年龄")
    private Integer age;

    @Schema(description = "筛查日期")
    private LocalDate screeningDate;

    @Schema(description = "审核状态：1-待审核，2-审核中，3-已审核")
    private Integer checkStatus;

    @Schema(description = "是否有阳性")
    private Boolean hasPositive;

    @Schema(description = "阳性项目")
    private String positiveItems;

    @Schema(description = "审核医生")
    private String auditDoctorName;

    @Schema(description = "审核时间")
    private LocalDateTime auditTime;

    @Schema(description = "复筛状态：0-无需复筛，1-待复筛，2-复筛中，3-已完成")
    private Integer recheckStatus;

    @Schema(description = "随访状态：0-无需随访，1-待随访，2-随访中，3-已完成")
    private Integer followStatus;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}