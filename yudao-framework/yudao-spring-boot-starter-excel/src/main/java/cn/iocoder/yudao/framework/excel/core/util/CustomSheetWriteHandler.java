package cn.iocoder.yudao.framework.excel.core.util;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import lombok.Data;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.util.List;
import java.util.Map;

@Data
public class CustomSheetWriteHandler implements SheetWriteHandler {

    private Map<Integer, List<String>> map;

    /**
     * 想实现Excel引用其他sheet页数据作为单元格下拉选项值，
     * 需要重写该方法
     *
     * @param writeWorkbookHolder
     * @param writeSheetHolder
     */
    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        // 构造样例数据，该数据可根据实际需要，换成业务数据
        // 实际数据可通过构造方法，get、set方法等由外界传入
        Map<Integer, List<String>> selectParamMap = this.getMap();

        // 获取第一个sheet页
        Sheet sheet = writeSheetHolder.getCachedSheet();
        // 获取sheet页的数据校验对象
        DataValidationHelper helper = sheet.getDataValidationHelper();

        // 获取工作簿对象，用于创建存放下拉数据的字典sheet数据页
        Workbook workbook = writeWorkbookHolder.getWorkbook();

        // 迭代索引，用于存放下拉数据的字典sheet数据页命名
        int index = 1;
        for (Map.Entry<Integer, List<String>> entry : selectParamMap.entrySet()) {

            // 设置存放下拉数据的字典sheet，并把这些sheet隐藏掉，这样用户交互更友好
            String dictSheetName = "dict_hide_sheet" + index;
            Sheet dictSheet = workbook.createSheet(dictSheetName);
            // 隐藏字典sheet页
            workbook.setSheetHidden(index++, true);

            // 取得下拉列表的数据
            List<String> values = entry.getValue();
            int rowLen = values.size();

            // 设置下拉列表覆盖的行数，从第一行开始到最后一行
            CellRangeAddressList infoList = new CellRangeAddressList(1, 1048575, entry.getKey(), entry.getKey());

            if (rowLen > 0) {
                // 向字典sheet写数据，从第一行开始写
                for (int i = 0; i < rowLen; i++) {
                    dictSheet.createRow(i).createCell(0).setCellValue(values.get(i));
                }

                // 设置关联数据公式
                String refers = dictSheetName + "!$A$1:$A$" + rowLen;
                Name name = workbook.createName();
                name.setNameName(dictSheetName);
                name.setRefersToFormula(refers);

                // 将上面设置好的下拉列表字典sheet页和目标sheet关联起来
                DataValidationConstraint constraint = helper.createFormulaListConstraint(dictSheetName);
                DataValidation dataValidation = helper.createValidation(constraint, infoList);
                dataValidation.setShowErrorBox(true); // 显示错误提示框
                dataValidation.setSuppressDropDownArrow(true); // 隐藏下拉箭头，实现多选
                sheet.addValidationData(dataValidation);
            }
        }
    }


}
