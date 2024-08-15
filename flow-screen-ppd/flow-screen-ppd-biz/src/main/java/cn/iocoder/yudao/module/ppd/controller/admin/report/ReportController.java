package cn.iocoder.yudao.module.ppd.controller.admin.report;


import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.ppd.controller.admin.report.vo.FilmingReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.report.vo.Index;
import cn.iocoder.yudao.module.ppd.controller.admin.report.vo.SummaryReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.report.vo.SummaryRespVO;
import cn.iocoder.yudao.module.ppd.dal.mysql.report.ReportMapper;
import cn.iocoder.yudao.module.ppd.service.report.ReportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 报表数据")
@RestController
@RequestMapping("/tb/screen-report")
@Validated
public class ReportController {
    @Resource
    private ReportService reportService;
    @Resource
    private ReportMapper reportMapper;

    /**
     * 获取本年份季度的报表指标数据
     * @return 报表指标数据集合
     */
    @GetMapping("/getReportData")
    public CommonResult<List<Index>> getReportData(@RequestParam("moreType") Integer moreType,
                                                   @RequestParam("year") Integer year,
                                                   @RequestParam(value = "quarter", required = false) Integer quarter,
                                                   @RequestParam("screenType") Integer screenType){
        List<Index> data = reportService.getReportData(moreType, year, quarter, screenType);
        FilmingReqVO filmingReqVO = new FilmingReqVO();
        filmingReqVO.setYear(year).setQuarter(quarter).setMoreType(moreType).setScreenType(screenType);
        reportService.setSomeData(data.get(0),filmingReqVO);
        return success(data);
    }


    /**
     * 学校肺结核筛查结果统计表
     */
    @GetMapping("/getSchoolSummary")
    public CommonResult<List<SummaryRespVO>> getSchoolSummary(@Validated SummaryReqVO summaryReqVO) {
        List<SummaryRespVO> list = reportService.getSchoolSummary(summaryReqVO.getDistrictCode(),
                summaryReqVO.getYear(), summaryReqVO.getScreenPoint(), summaryReqVO.getType());
        return success(list);
    }


    /**
     * 导出学校肺结核筛查结果统计表
     */
    @GetMapping("/exportSchoolSummary")
    public void exportSchoolSummary(@Validated SummaryReqVO summaryReqVO,
                                                              HttpServletResponse response) {
        List<SummaryRespVO> list = reportService.getSchoolSummary(summaryReqVO.getDistrictCode(),
                summaryReqVO.getYear(), summaryReqVO.getScreenPoint(), summaryReqVO.getType());
        reportService.exportSchoolSummary(response, list);
    }





    @GetMapping("/getAgencySummary")
    public CommonResult<Boolean> getAgencySummary(@RequestParam(value = "districtCode", required = false) String districtCode,
                                                  @RequestParam("year") Integer year,
                                                  @RequestParam("screenPoint") String screenPoint,
                                                  @RequestParam("type") Integer type) {

        return success(true);
    }


}
