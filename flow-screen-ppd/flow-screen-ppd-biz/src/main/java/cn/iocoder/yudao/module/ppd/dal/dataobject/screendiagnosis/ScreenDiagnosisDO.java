package cn.iocoder.yudao.module.ppd.dal.dataobject.screendiagnosis;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import org.glassfish.jaxb.core.v2.TODO;

import java.time.LocalDateTime;

/**
 * 诊断组 DO
 *
 * @author 芋道源码
 */
@TableName("tb_screen_diagnosis")
@KeySequence("tb_screen_diagnosis_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScreenDiagnosisDO extends BaseDO {

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
     * 医生签名
     */
    private String doctorSignature;
    /**
     * 筛查时间
     */
    private LocalDateTime screenTime;
    /**
     * 是否网报 0-否 1-是
     *
     * 枚举 {@link TODO tb_screen_diagnosis_report 对应的类}
     */
    private Integer report;
    /**
     * 符合潜伏治疗条件者是否进行预防性治疗 0-否 1-是
     *
     * 枚举 {@link TODO tb_screen_preventive_treatment 对应的类}
     */
    private Integer preventiveTreatment;
    /**
     * 筛查次序
     */
    private Integer screenOrder;
    /**
     * 对应摸底表中id
     */
    private Long personId;
    /**
     * 筛查点
     */
    private String screenPoint;
    /**
     * 诊断结果：1-疑似肺结核 2-肺结核 3-肺外结核、4-其他
     */
    private Integer outcome;
    /**
     * 治疗方案: 1=门诊治疗、2=住院治疗、3=门诊+住院治疗
     */
    private Integer treatmentProgram;
    /**
     * 备注
     */
    private String remark;

    /**
     * 筛查类型
     */
    private Integer screenType;
    /**
     * 筛查年份
     */
    private Integer year;
}