package cn.iocoder.yudao.module.childhealth.dal.dataobject.childbase;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("family_info")
public class FamilyInfoDO {
    @TableId(type = IdType.AUTO) private Long id;
    private Long childId;
    private String fatherName;
    private Integer fatherAge;
    private String fatherEducation;
    private String fatherOccupation;
    private String motherName;
    private Integer motherAge;
    private String motherEducation;
    private String motherOccupation;
    private String familyIncome;
    private String address;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
