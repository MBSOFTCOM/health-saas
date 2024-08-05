package cn.iocoder.yudao.module.ppd.controller.admin.screenppd.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - ppd组记录分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ScreenPpdPageReqVO extends PageParam {

    @Schema(description = "筛查编号(生成的编码)", example = "2195")
    private String screenId;

    @Schema(description = "同步时唯一编码", example = "9954")
    private Long syncId;

    @Schema(description = "对应摸底表中id", example = "2249")
    private Long personId;

    @Schema(description = "横径 单位mm")
    private BigDecimal transverseDiameter;

    @Schema(description = "纵径 单位mm")
    private BigDecimal longitudinalDiameter;

    @Schema(description = "是否含有水泡/双圈/坏死/淋巴管炎/ 1-是 0-否")
    private Integer bleb;

    @Schema(description = "是否注射(1-是 0-否)")
    private Integer injection;

    @Schema(description = "注射方式（根据字典）")
    private Integer injectionWay;

    @Schema(description = "结果。1-感染 0-未感染")
    private Integer outcome;

    @Schema(description = "医生签名")
    private String doctorSignature;

    @Schema(description = "注射单位")
    private String injectionAgency;

    @Schema(description = "筛查次序")
    private Integer screenOrder;

    @Schema(description = "筛查时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] screenTime;

    @Schema(description = "工作年度")
    private Integer year;

    @Schema(description = "筛查类型")
    private Integer screenType;

    @Schema(description = "筛查点")
    private String screenPoint;

}