package cn.iocoder.yudao.module.childhealth.dal.dataobject.screeningconfig;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDate;

/**
 * 转出档案表 DO
 *
 * 对应表: transfer_out_record
 * 模块: C.五健筛查配置
 * 创建日期: 2026-07-20
 */
@TableName("transfer_out_record")
@KeySequence("transfer_out_record_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferOutRecordDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 转出编号
     */
    private String transferNo;

    /**
     * 儿童ID
     */
    private Long childId;

    /**
     * 目标机构
     */
    private String targetOrg;

    /**
     * 目标机构编码
     */
    private String targetOrgCode;

    /**
     * 转出日期
     */
    private LocalDate transferDate;

    /**
     * 转出原因
     */
    private String reason;

    /**
     * 阳性等级 1轻度 2中度 3重度
     */
    private Integer positiveLevel;

    /**
     * 后续反馈
     */
    private String feedback;

    /**
     * 跟踪状态 0待反馈 1已反馈 2已结案
     */
    private Integer followupStatus;

    /**
     * 档案URL
     */
    private String archiveUrl;

    /**
     * 备注
     */
    private String remark;

}
