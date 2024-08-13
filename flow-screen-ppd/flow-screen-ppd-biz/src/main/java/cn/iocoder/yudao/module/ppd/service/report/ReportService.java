package cn.iocoder.yudao.module.ppd.service.report;


import cn.iocoder.yudao.module.ppd.controller.admin.report.vo.FilmingReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.report.vo.Index;

import java.util.List;

public interface ReportService {
    /** 学生 */
    Integer STUDENT=1;
    /** 教职工 */
    Integer STAFF=4;
    /** 学校 1+4*/
     Integer SCHOOL=STUDENT+STAFF;
    /** 寺庙 */
    Integer MONK=32;
    /** 密接者 */
    Integer CLOSER=8;
    /** 老年人 */
    Integer OLD=2;
    /** 糖尿病 */
    Integer DIABETES=16;
    /** HIV/AIDS */
    Integer HIV=128;

    /**
     * 获取到报表数据
     * @return 报表数据
     */
    List<Index> getReportData(Integer moreType, Integer year, Integer quarter, Integer screenType);
    Index setSomeData(Index index, FilmingReqVO filmingReqVO);

    /**
     * 统计应拍片人数
     * @param reqVO FilmingReqVO
     * @return Integer
     */
    Integer getFilming(FilmingReqVO reqVO);
    /**
     * 根据人群分类统计应拍片人数
     * @param reqVO FilmingReqVO
     * @return Integer
     */
    Integer getFilmingByMoreType(FilmingReqVO reqVO);

    /**
     * 获取应采集痰的人数
     * @param reqVO FilmingReqVO
     * @return Integer
     */
    Integer getSputumCollection(FilmingReqVO reqVO);
    /**
     * 获取应采集痰的人数
     * @param reqVO FilmingReqVO
     * @return Integer
     */
    Integer getSputumCollectionByMoreType(FilmingReqVO reqVO);

    /**
     * 获取实际拍片人数
     * @param reqVO FilmingReqVO
     * @return Integer
     */
    Integer getActualTakeCtNum(FilmingReqVO reqVO);
    /**
     * 获取实际拍片人数
     * @param reqVO FilmingReqVO
     * @return Integer
     */
    Integer getActualSputumCollectionNum(FilmingReqVO reqVO);
}
