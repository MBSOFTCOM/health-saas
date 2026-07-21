package cn.iocoder.yudao.module.childhealth.controller.admin.crud.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class GradeClassImportVO {

    @ExcelProperty("学年名称")
    private String yearName;

    @ExcelProperty("年级名称")
    private String gradeName;

    @ExcelProperty("班级名称")
    private String className;

    @ExcelProperty("班主任")
    private String headTeacher;
}
