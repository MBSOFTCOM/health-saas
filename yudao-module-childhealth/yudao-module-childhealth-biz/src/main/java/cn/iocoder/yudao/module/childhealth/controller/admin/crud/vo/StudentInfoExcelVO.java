package cn.iocoder.yudao.module.childhealth.controller.admin.crud.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class StudentInfoExcelVO {

    @ExcelProperty("儿童ID")
    private Long childId;

    @ExcelProperty("学号")
    private String studentNo;

    @ExcelProperty("班级ID")
    private Long classId;

    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("性别")
    private Integer gender;

    @ExcelProperty("出生日期")
    private String birthDate;

    @ExcelProperty("监护人姓名")
    private String guardianName;

    @ExcelProperty("监护人电话")
    private String guardianMobile;

    @ExcelProperty("状态")
    private Integer status;
}
