package cn.iocoder.yudao.module.ppd.dal.dataobject.screensputumexamination;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 痰检组 DO
 *
 * @author 芋道源码
 */
@TableName("tb_screen_sputum_examination")
@KeySequence("tb_screen_sputum_examination_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScreenSputumExaminationDO extends BaseDO {

    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 年份
     */
    private Integer year;
    /**
     * 筛查类型
     */
    private Integer screenType;
    /**
     * 筛查编号
     */
    private String screenId;
    /**
     * 同步时唯一编码
     */
    private Long syncId;
    /**
     * 医生签名
     */
    private String doctorSignature;
    /**
     * 是否雾化(0-否，1-是)
     */
    private Integer atomization;
    /**
     * 是否痰检(0-未痰检，1-已经痰检)
     */
    private Integer sputumExamination;
    /**
     * 痰标本类型，1-无痰 2-即时痰 3-发放晨痰 4-夜间痰盒
     */
    private Integer type;
    /**
     * 即时痰照片
     */
    private String forthwithSputum;
    /**
     * 即时痰标本号
     */
    private String forthwithSputumCode;
    /**
     * 筛查时间
     */
    private LocalDateTime screenTime;
    /**
     * 筛查次序
     */
    private Integer screenOrder;
    /**
     * 对应摸底表中id
     */
    private Long personId;
    /**
     * 对应摸底表中患者姓名
     */
    @TableField(exist = false)
    private String name;
    /**
     * 痰照片
     */
    private String eveningSputum;
    /**
     * 夜痰标本号
     */
    private String eveningSputumCode;
    /**
     * 晨痰照片
     */
    private String morningSputum;
    /**
     * 晨痰标本号
     */
    private String morningSputumCode;
    /**
     * 结果？？？
     */
    private String outcome;

    /**
     * 即时痰照片采集时间
     */
    private LocalDateTime forthwithSputumTime;
    /**
     * 夜痰照片采集时间
     */
    private LocalDateTime eveningSputumTime;
    /**
     * 晨痰照片采集时间
     */
    private LocalDateTime morningSputumTime;

}