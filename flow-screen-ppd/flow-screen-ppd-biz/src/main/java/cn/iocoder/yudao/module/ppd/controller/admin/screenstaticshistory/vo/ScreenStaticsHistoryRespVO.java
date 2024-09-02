package cn.iocoder.yudao.module.ppd.controller.admin.screenstaticshistory.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - 工作进展报告-统计表-导出的历史选项 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ScreenStaticsHistoryRespVO {

    @Schema(description = "主键id", requiredMode = Schema.RequiredMode.REQUIRED, example = "5082")
    @ExcelProperty("主键id")
    private Long id;

    @Schema(description = "机构id", example = "31615")
    @ExcelProperty("机构id")
    private Long deptId;

    @Schema(description = "表格标题")
    @ExcelProperty("表格标题")
    private String tableTittle;

    @Schema(description = "学校名称")
    @ExcelProperty("学校名称")
    private String school;

    @Schema(description = "医院名称")
    @ExcelProperty("医院名称")
    private String hospital;

    @Schema(description = "行政区划名称")
    @ExcelProperty("行政区划名称")
    private String district;

    @Schema(description = "联系人姓名")
    @ExcelProperty("联系人姓名")
    private String contact;

    @Schema(description = "联系电话")
    @ExcelProperty("联系电话")
    private String contactPhone;

    @Schema(description = "注射人姓名")
    @ExcelProperty("注射人姓名")
    private String injectionPeople;

    @Schema(description = "查验人姓名")
    @ExcelProperty("查验人姓名")
    private String checkPeople;

    @Schema(description = "基本信息勾选，如'[1,2,3,4]'")
    @ExcelProperty("基本信息勾选，如'[1,2,3,4]'")
    private List<Integer> infoList;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}