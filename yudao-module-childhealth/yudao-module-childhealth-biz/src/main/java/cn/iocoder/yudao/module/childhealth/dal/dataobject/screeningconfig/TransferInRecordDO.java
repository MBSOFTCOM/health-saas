package cn.iocoder.yudao.module.childhealth.dal.dataobject.screeningconfig;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDate;

/**
 * 转入档案表 DO
 *
 * 对应表: transfer_in_record
 * 模块: C.五健筛查配置
 * 创建日期: 2026-07-20
 */
@TableName("transfer_in_record")
@KeySequence("transfer_in_record_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferInRecordDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 转入编号
     */
    private String transferNo;

    /**
     * 儿童ID
     */
    private Long childId;

    /**
     * 来源机构
     */
    private String sourceOrg;

    /**
     * 来源机构编码
     */
    private String sourceOrgCode;

    /**
     * 转入日期
     */
    private LocalDate transferDate;

    /**
     * 接诊状态 0待接诊 1已接诊 2已拒绝
     */
    private Integer receiveStatus;

    /**
     * 接诊医生ID
     */
    private Long receiveDoctorId;

    /**
     * 接诊日期
     */
    private LocalDate receiveDate;

    /**
     * 历史档案URL
     */
    private String historyArchiveUrl;

    /**
     * 后续随访计划
     */
    private String followupPlan;

    /**
     * 转入原因
     */
    private String reason;

    /**
     * 备注
     */
    private String remark;

}
