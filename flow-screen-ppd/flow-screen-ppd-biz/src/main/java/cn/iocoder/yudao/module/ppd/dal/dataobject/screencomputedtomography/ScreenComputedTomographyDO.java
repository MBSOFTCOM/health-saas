package cn.iocoder.yudao.module.ppd.dal.dataobject.screencomputedtomography;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * ct、dr组 DO
 *
 * @author 芋道源码
 */
@TableName("tb_screen_computed_tomography")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScreenComputedTomographyDO extends BaseDO {

    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 筛查编号
     */
    private String screenId;
    /**
     * 同步时唯一编码
     */
    private Long syncId;
    /**
     * 胸片编号
     */
    private String computedTomographyCode;
    /**
     * 胸片
     */
    private String computedTomography;
    /**
     * 医生签名
     */
    private String doctorSignature;
    /**
     * 筛查时间
     */
    private LocalDateTime screenTime;
    /**
     * 拍照时间
     */
    private LocalDateTime photoTime;
    /**
     * 筛查次序
     */
    private Integer screenOrder;
    /**
     * 年份
     */
    private Integer year;
    /**
     * 筛查类型  1--常规、2--新生、3--应急
     */
    private Integer screenType;
    /**
     * 对应摸底表中id
     */
    private Long personId;
    /**
     * 结果。2-其他异常 1-疑似结核 0-无异常
     */
    private Integer outcome;
    /**
     * 其他异常说明
     */
    private String remark;

}