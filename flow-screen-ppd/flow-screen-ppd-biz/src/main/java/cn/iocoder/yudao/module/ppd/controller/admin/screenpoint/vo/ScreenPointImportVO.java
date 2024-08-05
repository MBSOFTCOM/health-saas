package cn.iocoder.yudao.module.ppd.controller.admin.screenpoint.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = false) // 设置 chain = false，避免用户导入有问题
public class ScreenPointImportVO implements Serializable {

    @ExcelProperty("筛查点名称")
    private String name;

    @ExcelProperty("筛查单位")
    private String screenDept;

    @ExcelProperty("工作年度")
    private Integer year;
}
