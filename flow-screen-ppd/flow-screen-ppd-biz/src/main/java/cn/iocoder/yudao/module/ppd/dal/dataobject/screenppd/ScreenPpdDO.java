package cn.iocoder.yudao.module.ppd.dal.dataobject.screenppd;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * ppd组记录 DO
 *
 * @author 芋道源码
 */
@TableName("tb_screen_ppd")
@KeySequence("tb_screen_ppd_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScreenPpdDO extends BaseDO {

    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 筛查编号(生成的编码)
     */
    private String screenId;
    /**
     * 同步时唯一编码
     */
    private Long syncId;
    /**
     * 对应摸底表中id
     */
    private Long personId;
    /**
     * 对应摸底表中患者姓名
     */
    private String name;

    @Schema(description = "身份证")
    private String idNum;

    @Schema(description = "试剂批次id", example = "9954")
    private Long reagentId;

    @Schema(description = "转换系数")
    private Integer reagentSpecsNum;

    @Schema(description = "ppd实拍图")
    private String actualPhoto;

    @Schema(description = "红晕编辑图")
    private String blushPhoto;

    @Schema(description = "硬结编辑图")
    private String scleromaPhoto;

    @Schema(description = "硬结横径 单位mm")
    private BigDecimal transverseDiameter;

    @Schema(description = "硬结纵径 单位mm")
    private BigDecimal longitudinalDiameter;

    @Schema(description = "红晕横径 单位mm")
    private BigDecimal blushTransverseDiameter;

    @Schema(description = "纵径纵径 单位mm")
    private BigDecimal blushLongitudinalDiameter;
    /**
     * 是否含有水泡/双圈/坏死/淋巴管炎/ 1-是 0-否
     */
    private Integer bleb;
    /**
     * 是否注射(1-是 0-否)
     */
    private Integer injection;
    /**
     * 注射方式（根据字典）
     */
    private Integer injectionWay;
    /**
     * 结果。1-感染 0-未感染
     */
    private Integer outcome;
    /**
     * 医生签名
     */
    private String doctorSignature;
    /**
     * 注射单位
     */
    private String injectionAgency;
    /**
     * 筛查次序
     */
    private Integer screenOrder;
    /**
     * 筛查时间
     */
    private LocalDateTime screenTime;

    /**
     * 工作年度
     */
    private Integer year;

    /**
     * 筛查类型
     */
    private Integer screenType;

    private String padId;

}