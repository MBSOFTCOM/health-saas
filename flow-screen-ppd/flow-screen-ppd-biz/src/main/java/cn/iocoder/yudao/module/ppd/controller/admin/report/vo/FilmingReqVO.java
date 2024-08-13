package cn.iocoder.yudao.module.ppd.controller.admin.report.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class FilmingReqVO {
    /** 筛查类型*/
    private Integer screenType;
    /** 患者的多人群分类*/
    private Integer moreType;
    /** 按照季度*/
    @Range(min=1,max = 4,message = "季度的范围是1~4")
    private Integer quarter;
    /** 年份*/
    @Range(min=2023,message = "年度最早只到2023年")
    private Integer year;
    /** 最小年龄范围*/
    private Integer leftAge;
    /** 最大年龄范围*/
    private Integer rightAge;
    /** 患者id*/
    private List<Long> personIds;

    /**
     * 初始化季度参数，若无季度参数，则获取当前日期的季度
     * @param quarter Integer
     * @return Integer
     */
    public Integer initQuarter(Integer quarter){
        if (quarter == null || quarter < 1 || quarter > 4){
            // 获取当前日期
            LocalDate currentDate = LocalDate.now();

            // 获取当前月份
            Month currentMonth = currentDate.getMonth();

            // 计算当前季度
            Integer quarterValue = (currentMonth.getValue() - 1) / 3 + 1;
            this.quarter=quarterValue;
        }else {
            this.quarter=quarter;
        }
        return this.quarter;
    }
    /**
     * 初始化季度参数，若无季度参数，则获取当前日期的季度
     * @return Integer
     */
    public Integer initQuarter(){
        return initQuarter(this.getQuarter());
    }
    /**
     * 初始化季度参数，若无季度参数，则获取当前日期的季度
     * @return Integer
     */
    public Integer initYear(){
        if (this.getYear() == null){
            this.year=LocalDate.now().getYear();
        }else {
            this.year=this.getYear();
        }
        return this.year;
    }

}
