package cn.iocoder.yudao.module.childhealth.dal.dataobject.workflow;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("external_report")
public class ExternalReportDO {
    @TableId(type = IdType.AUTO) private Long id;
    private Long childId;
    private String reportType;
    private LocalDate reportDate;
    private String reportHospital;
    private String reportContent;
    private String fileUrl;
    private LocalDateTime uploadTime;
    private LocalDateTime createTime;
}
