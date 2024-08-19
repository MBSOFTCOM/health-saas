package cn.iocoder.yudao.module.ppd.controller.admin.screencollect.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 采集 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ScreenCollectRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "5998")
    @ExcelProperty("主键")
    private Long id;

    @Schema(description = "筛查编号", example = "10874")
    @ExcelProperty("筛查编号")
    private String screenId;
    /**
     * 身份证
     */
    private String idNum;
    private Integer age;
    private Integer contacted;
    private String schoolName;
    private String classroom;
    private String tel;

    @Schema(description = "同步时唯一编码", example = "13599")
    @ExcelProperty("同步时唯一编码")
    private Long syncId;

    @Schema(description = "医生签名")
    @ExcelProperty("医生签名")
    private String doctorSignature;

    @Schema(description = "筛查单位")
    @ExcelProperty("筛查单位")
    private String screenAgency;

    @Schema(description = "筛查时间")
    @ExcelProperty("筛查时间")
    private LocalDateTime screenTime;

    @Schema(description = "筛查次序")
    @ExcelProperty("筛查次序")
    private Integer screenOrder;

    @Schema(description = "对应摸底表中id", example = "19490")
    @ExcelProperty("对应摸底表中id")
    private Long personId;

    @Schema(description = "结果（分新生筛查和其他）。	新生：1-咳嗽、咳痰不小于2周 2-痰中带血或咯血 3-反复发热2周以上 4-淋巴结肿大	其他：1-咳嗽、咳痰（超过一周）2-血痰或咯血 3-发热	4--胸痛 5-夜间盗汗 6-食欲不振 7-乏力 8-体重减轻（超过6斤 9-卡痕异常	")
    @ExcelProperty("结果（分新生筛查和其他）。	新生：1-咳嗽、咳痰不小于2周 2-痰中带血或咯血 3-反复发热2周以上 4-淋巴结肿大	其他：1-咳嗽、咳痰（超过一周）2-血痰或咯血 3-发热	4--胸痛 5-夜间盗汗 6-食欲不振 7-乏力 8-体重减轻（超过6斤 9-卡痕异常	")
    private String outcome;

    @Schema(description = "筛查点")
    @ExcelProperty("筛查点")
    private String screenPoint;

    @Schema(description = "年份")
    private Integer year;

    @Schema(description = "筛查类型")
    private Integer screenType;

    /**
     * 对应摸底表中患者姓名
     */
    private String name;
}