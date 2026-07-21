package cn.iocoder.yudao.module.childhealth.dal.dataobject.casemanagement;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDate;

/**
 * 专案主表 DO
 *
 * 对应表: case_management
 * 模块: B.专案管理
 * 创建日期: 2026-07-20
 */
@TableName("case_management")
@KeySequence("case_management_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CaseManagementDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 专案编号
     */
    private String caseNo;

    /**
     * 儿童ID
     */
    private Long childId;

    /**
     * 专案类型 1高危儿 2营养不良 3肥胖 4贫血 5佝偻病 6发育行为异常
     */
    private Integer caseType;

    /**
     * 专案子类型（如：早产儿/低体重/高胆红素血症/遗传代谢/HIE/生长迟缓/消瘦/超重等）
     */
    private String caseSubtype;

    /**
     * 建立方式 0自动 1手动
     */
    private Integer caseSource;

    /**
     * 来源记录ID（体检/筛查/评估）
     */
    private Long sourceRecordId;

    /**
     * 专案等级 1轻度 2中度 3重度
     */
    private Integer caseLevel;

    /**
     * 标签JSON
     */
    private String caseTags;

    /**
     * 建立日期
     */
    private LocalDate establishDate;

    /**
     * 结案日期
     */
    private LocalDate closeDate;

    /**
     * 结案原因
     */
    private String closeReason;

    /**
     * 结案类型 1康复达标 2转介 3其他
     */
    private Integer closeType;

    /**
     * 状态 0进行中 1已结案 2已取消
     */
    private Integer status;

    /**
     * 责任医生ID
     */
    private Long responsibleDoctorId;

    /**
     * 责任医生姓名
     */
    private String responsibleDoctorName;

    /**
     * 个案卡文件URL
     */
    private String caseCardUrl;

    /**
     * 专案描述
     */
    private String description;

}
