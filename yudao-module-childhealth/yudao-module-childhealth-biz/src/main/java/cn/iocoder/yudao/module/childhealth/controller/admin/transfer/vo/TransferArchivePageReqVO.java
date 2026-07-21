package cn.iocoder.yudao.module.childhealth.controller.admin.transfer.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

@Schema(description = "管理后台 - 档案转递分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TransferArchivePageReqVO extends PageParam {

    @Schema(description = "转递编号", example = "ZD202401010001")
    private String transferNo;

    @Schema(description = "儿童ID", example = "1")
    private Long childId;

    @Schema(description = "转递类型", example = "1")
    private Integer transferType;

    @Schema(description = "转递日期")
    private LocalDate[] transferDate;

    @Schema(description = "状态", example = "0")
    private Integer status;

}