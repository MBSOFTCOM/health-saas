package cn.iocoder.yudao.module.childhealth.dal.dataobject.ops;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 工作量统计表 DO
 *
 * 对应表: staff_workload_statistics
 * 模块: 20. 全维度数据统计
 * 创建日期: 2026-07-20
 */
@TableName("staff_workload_statistics")
@KeySequence("staff_workload_statistics_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StaffWorkloadStatisticsDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 统计日期
     */
    private LocalDate statDate;

    /**
     * 医护ID（关联staff_info.id）
     */
    private Long staffId;

    /**
     * 医护姓名（冗余）
     */
    private String staffName;

    /**
     * 科室ID
     */
    private Long deptId;

    /**
     * 机构ID
     */
    private Long orgId;

    /**
     * 机构名称
     */
    private String orgName;

    /**
     * 关联学校ID（按学校统计）
     */
    private Long schoolId;

    /**
     * 筛查数
     */
    private Integer screeningCount;

    /**
     * 审核数
     */
    private Integer auditCount;

    /**
     * 随访数
     */
    private Integer followCount;

    /**
     * 复筛数
     */
    private Integer recheckCount;

    /**
     * 转介数
     */
    private Integer referralCount;

    /**
     * 专案创建数
     */
    private Integer caseCreateCount;

    /**
     * 专案结案数
     */
    private Integer caseCloseCount;

    /**
     * 总工作量(加权得分)
     */
    private BigDecimal totalWorkload;

    /**
     * 扩展指标 JSON
     */
    private String extraMetrics;

}
