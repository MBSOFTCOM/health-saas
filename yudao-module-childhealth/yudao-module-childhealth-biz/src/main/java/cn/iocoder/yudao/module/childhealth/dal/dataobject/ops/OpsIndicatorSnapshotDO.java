package cn.iocoder.yudao.module.childhealth.dal.dataobject.ops;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 运营指标日快照表 DO
 *
 * 对应表: ops_indicator_snapshot
 * 模块: 1. 数据看板 + 12. 运营工作台
 * 创建日期: 2026-07-20
 */
@TableName("ops_indicator_snapshot")
@KeySequence("ops_indicator_snapshot_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpsIndicatorSnapshotDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 快照日期
     */
    private LocalDate snapshotDate;

    /**
     * 批次ID
     */
    private Long batchId;

    /**
     * 学校ID
     */
    private Long schoolId;

    /**
     * 年级ID
     */
    private Long gradeId;

    /**
     * 行政区划代码
     */
    private String regionCode;

    /**
     * 参检率%
     */
    private BigDecimal participationRate;

    /**
     * 阳性率%
     */
    private BigDecimal positiveRate;

    /**
     * 复筛率%
     */
    private BigDecimal recheckRate;

    /**
     * 随访率%
     */
    private BigDecimal followRate;

    /**
     * 随访完成率%
     */
    private BigDecimal followCompleteRate;

    /**
     * 筛查总人数
     */
    private Integer screeningCount;

    /**
     * 应检人数
     */
    private Integer targetCount;

    /**
     * 实检人数
     */
    private Integer actualCount;

    /**
     * 阳性人数
     */
    private Integer positiveCount;

    /**
     * 复筛人数
     */
    private Integer recheckCount;

    /**
     * 随访人数
     */
    private Integer followCount;

    /**
     * 待审核数
     */
    private Integer pendingAuditCount;

    /**
     * 待复筛数
     */
    private Integer pendingRecheckCount;

    /**
     * 待随访数
     */
    private Integer pendingFollowCount;

    /**
     * 五健专项分类统计 JSON
     */
    private String categoryStats;

    /**
     * 扩展指标 JSON
     */
    private String extraIndicators;

}
