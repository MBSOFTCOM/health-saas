package cn.iocoder.yudao.module.ppd.dal.mysql.report;


import cn.iocoder.yudao.module.ppd.controller.admin.report.vo.FilmingReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.report.vo.SummaryRespVO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@InterceptorIgnore(tenantLine = "true")
public interface ReportMapper {
    /**
     * 采集组、ppd 至少一个异常的人数;主要用于统计6-14岁学生、僧尼和0-5岁非重点人群的应拍片数
     * @param reqVO FilmingReqVO
     * @return List<Long>
     */
    Integer getCollectAndPpdErrorNum(FilmingReqVO reqVO);
    /**
     * ppd 异常的人数;主要用于统计全流程为一条线的人群 ：0-5岁学生 和 僧尼
     * @param reqVO FilmingReqVO
     * @return List<Long>
     */
    Integer getPpdErrorNum(FilmingReqVO reqVO);

    /**
     * 获取实际拍片人数
     * @param reqVO FilmingReqVO
     * @return Integer
     */
    Integer getActualTakeCtNum(FilmingReqVO reqVO);
    /**
     * 获取实际采集痰人数
     * @param reqVO FilmingReqVO
     * @return Integer
     */
    Integer getActualSputumCollectionNum(FilmingReqVO reqVO);

    /**
     * 根据人群分类 和年龄最小范围查询患者id
     * @param reqVO FilmingReqVO
     * @return List<Long>
     */
    List<Long> listPersonByTypeAndMinAge(FilmingReqVO reqVO);

    /**
     * 根据人群分类 和年龄最大最小范围查询患者id
     * @param reqVO FilmingReqVO
     * @return List<Long>
     */
    List<Long> listPersonByTypeAndAge(FilmingReqVO reqVO);

    /**
     * 统计胸片有异常的
     * @param reqVO FilmingReqVO
     * @return Integer
     */
    Integer countCtOrDrError(FilmingReqVO reqVO);
    /**
     * 统计胸片或ppd有异常的
     * @param reqVO FilmingReqVO
     * @return Integer
     */
    Integer countCollectOrCtOrDrError(FilmingReqVO reqVO);
    /**
     * 统计采集症状或胸片或ppd有异常的
     * @param reqVO FilmingReqVO
     * @return Integer
     */
    Integer countCollectOrPpdOrCtOrDrError(FilmingReqVO reqVO);

    List<SummaryRespVO> getSchoolSummary(@Param("year") Integer year,
                                         @Param("districtCode") String districtCode,
                                         @Param("type") Integer type);

    List<SummaryRespVO> getSchoolSummary2(@Param("year") Integer year,
                                          @Param("districtCode") String districtCode,
                                          @Param("type") Integer type);

    List<SummaryRespVO> getSchoolSummary3(@Param("year") Integer year,
                                          @Param("districtCode") String districtCode,
                                          @Param("type") Integer type);
}
