package cn.iocoder.yudao.module.ppd.dal.dataobject.screensum;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 汇总 DO
 *
 * @author 芋道源码
 */
@TableName("tb_screen_sum")
@KeySequence("tb_screen_sum_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScreenSumDO {
    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 筛查年份
     */
    private Integer year;
    /**
     * 筛查编号
     */
    private String screenId;
    /**
     * 身份证
     */
    private String idNum;
    /**
     * 对应摸底表中id
     */
    private Long personId;
    /**
     * 同步时唯一编码
     */
    private Long syncId;
    /**
     * 最近一次采集时间
     */
    private LocalDateTime lastCollectTime;
    /**
     * 采集次数
     */
    private Integer collectNum;
    /**
     * 最近一次tst时间
     */
    private LocalDateTime lastPpdTime;
    /**
     * tst次数
     */
    private Integer ppdNum;
    /**
     * 最近一次做胸片时间
     */
    private LocalDateTime lastChestRadiographTime;
    /**
     * 最近一次痰检时间
     */
    private LocalDateTime lastSputumExaminationTime;
    /**
     * 痰检次数
     */
    private Integer sputumExaminationNum;
    /**
     * 最近一次心电图时间
     */
    private LocalDateTime lastElectrocardiogramTime;
    /**
     * 心电图次数
     */
    private Integer electrocardiogramNum;
    /**
     * 最近一次诊断时间
     */
    private LocalDateTime lastDiagnosisTime;
    /**
     * 诊断次数
     */
    private Integer diagnosisNum;
    /**
     * 采集表id
     */
    private Long collectId;
    /**
     * ppd表id
     */
    private Long ppdId;
    /**
     * 痰检表id
     */
    private Long sputumExaminationId;
    /**
     * 心电图表id
     */
    private Long electrocardiogramId;
    /**
     * 诊断表id
     */
    private Long diagnosisId;
    /**
     * 筛查类型
     */
    private Integer screenType;
    /**
     * dr次数
     */
    private Integer chestRadiographNum;
    /**
     * 最近一次做ct的时间
     */
    private LocalDateTime lastComputedTomographyTime;
    /**
     * ct次数
     */
    private Integer computedTomographyNum;
    /**
     * 最近一次实验组时间
     */
    private LocalDateTime lastExperimentTime;
    /**
     * 实验次数
     */
    private Integer experimentNum;
    /**
     * 当前已完成的分组
     */
    private String curFinish;
    /**
     * dr胸片表id
     */
    private Long chestRadiographId;
    /**
     * ct表id
     */
    private Long computedTomographyId;
    /**
     * 实验组id
     */
    private Long experimentId;

}