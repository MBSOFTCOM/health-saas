package cn.iocoder.yudao.module.ppd.controller.admin.screenpersonrealsituation.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentInfo extends StudentInfoReqVO{
    private String year;

    public StudentInfo(Long id, String name, String phone, String idcard, String schoolName, String className,String year) {
        super(id, name, phone, idcard, schoolName, className);
        this.year=year;
    }
}
