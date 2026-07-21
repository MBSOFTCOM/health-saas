package cn.iocoder.yudao.module.childhealth.dal.dataobject.workflow;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("transfer_archive")
public class TransferArchiveDO extends BaseDO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String transferNo;
    private Long childId;
    private Integer transferType;
    private LocalDate transferDate;
    private String sourceHospital;
    private String targetHospital;
    private String transferReason;
    private String healthSummary;
    private String caseList;
    private String contactPerson;
    private String contactMobile;
    private String feedbackContent;
    private Integer status;
}
