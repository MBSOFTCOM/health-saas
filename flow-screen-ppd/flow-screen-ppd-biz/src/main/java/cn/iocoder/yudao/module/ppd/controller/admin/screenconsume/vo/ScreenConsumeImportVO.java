package cn.iocoder.yudao.module.ppd.controller.admin.screenconsume.vo;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import cn.iocoder.yudao.module.ppd.controller.admin.screenreagent.vo.ScreenReagentImportVO;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 消耗管理 Response VO")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = false) // 设置 chain = false，避免用户导入有问题
@ExcelIgnoreUnannotated
public class ScreenConsumeImportVO {

    @Schema(description = "试剂名称")
    @ExcelProperty("试剂名称")
    private String reagentName;

    @Schema(description = "消耗序位")
    @ExcelProperty("消耗序位")
    private Integer consumeOrder;

    @Schema(description = "批次号")
    @ExcelProperty("批次号")
    private String bathNumber;

    @Schema(description = "入库量（按试剂）")
    @ExcelProperty("入库量")
    private Integer inboundNumber;

    @Schema(description = "生产日期")
    @ExcelProperty("生产日期")
    private String manufactureDate;

    @Schema(description = "有效期")
    @ExcelProperty("有效期(天)")
    private String indate;

    public boolean isEmpty(ScreenConsumeImportVO respVO) {
        ScreenConsumeImportVO vo = new ScreenConsumeImportVO();
        return vo.equals(respVO);
    }

}