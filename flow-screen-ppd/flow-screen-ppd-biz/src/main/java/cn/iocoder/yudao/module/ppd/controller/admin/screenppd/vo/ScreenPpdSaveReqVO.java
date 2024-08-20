package cn.iocoder.yudao.module.ppd.controller.admin.screenppd.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - ppd组记录新增/修改 Request VO")
@Data
public class ScreenPpdSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "26824")
    private Long id;

    @Schema(description = "筛查编号(生成的编码)", example = "2195")
    private String screenId;

    @Schema(description = "身份证")
    private String idNum;

    @Schema(description = "同步时唯一编码", example = "9954")
    private Long syncId;

    @Schema(description = "试剂批次id", example = "9954")
    private Long reagentId;

    @Schema(description = "转换系数")
    private Integer reagentSpecsNum;

    @Schema(description = "对应摸底表中id", example = "2249")
    private Long personId;

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

    @Schema(description = "ppd实拍图")
    private String actualPhoto;

    @Schema(description = "红晕编辑图")
    private String blushPhoto;

    @Schema(description = "硬结编辑图")
    private String scleromaPhoto;


    @Schema(description = "注射单位")
    private String injectionAgency;

    @Schema(description = "筛查次序")
    private Integer screenOrder;

    @Schema(description = "筛查时间")
    private LocalDateTime screenTime;

    @Schema(description = "工作年度")
    private Integer year;

    @Schema(description = "筛查类型")
    private Integer screenType;

    @Schema(description = "硬结横径 单位mm")
    private BigDecimal transverseDiameter;

    @Schema(description = "硬结纵径 单位mm")
    private BigDecimal longitudinalDiameter;

    @Schema(description = "红晕横径 单位mm")
    private BigDecimal blushTransverseDiameter;

    @Schema(description = "纵径纵径 单位mm")
    private BigDecimal blushLongitudinalDiameter;

}