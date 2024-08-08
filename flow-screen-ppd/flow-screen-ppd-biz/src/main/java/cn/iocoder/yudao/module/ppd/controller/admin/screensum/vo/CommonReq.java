package cn.iocoder.yudao.module.ppd.controller.admin.screensum.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonReq {
    /**
     * 患者id
     */
    private Long personId;
    private String IdNum;
    /**
     * 年度
     */
    private String year;
    /** 筛查次序*/
    private Integer screenOrder;
    /** 筛查类型*/
    private Integer screenType;

    public CommonReq(String idNum, String year, Integer screenType) {
        IdNum = idNum;
        this.year = year;
        this.screenType = screenType;
    }

    public CommonReq(String idNum, String year, Integer screenOrder, Integer screenType) {
        IdNum = idNum;
        this.year = year;
        this.screenOrder = screenOrder;
        this.screenType = screenType;
    }

    public CommonReq(Long personId, String year, Integer screenOrder, Integer screenType) {
        this.personId = personId;
        this.year = year;
        this.screenOrder = screenOrder;
        this.screenType = screenType;
    }
}
