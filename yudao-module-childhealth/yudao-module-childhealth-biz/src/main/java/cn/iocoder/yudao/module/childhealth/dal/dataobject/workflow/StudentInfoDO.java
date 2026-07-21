package cn.iocoder.yudao.module.childhealth.dal.dataobject.workflow;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("student_info")
public class StudentInfoDO {
    @TableId(type = IdType.AUTO) private Long id;
    private Long childId;
    private String studentNo;
    private Long classId;
    private String name;
    private Integer gender;
    private LocalDate birthDate;
    private String guardianName;
    private String guardianMobile;
    private Integer status;
    private LocalDateTime createTime;
}
