package cn.iocoder.yudao.module.childhealth.dal.dataobject.childbase;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("neonatal_diagnosis")
public class NeonatalDiagnosisDO {
    @TableId(type = IdType.AUTO) private Long id;
    private Long childId;
    private String diagnosisCode;
    private String diagnosisName;
    private Integer diagnosisType;
    private LocalDate admissionDate;
    private LocalDate dischargeDate;
    private String hospitalName;
    private String department;
    private Boolean isAlerted;
    private Integer source;
    private LocalDateTime createTime;
}
