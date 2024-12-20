package cn.iocoder.yudao.module.ppd.dal.mysql.screenpersonrealsituation;


import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.ppd.controller.admin.screenpersonrealsituation.vo.*;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenchestradiograph.ScreenChestRadiographDO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screencollect.ScreenCollectDO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screencomputedtomography.ScreenComputedTomographyDO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenpersonrealsituation.ScreenPersonDO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenppd.ScreenPpdDO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 摸底 Mapper
 *
 * @author 侯卿
 */
@Mapper
@InterceptorIgnore(tenantLine = "true")
public interface ScreenPersonMapper extends BaseMapperX<ScreenPersonDO> {

    // 摸底管理分页
    default PageResult<ScreenPersonDO> selectPage(ScreenPersonPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ScreenPersonDO>()
                .eqIfPresent(ScreenPersonDO::getIdNum, reqVO.getIdNum())
                .eqIfPresent(ScreenPersonDO::getYear, reqVO.getYear())
                .likeIfPresent(ScreenPersonDO::getName, reqVO.getName())
                .eqIfPresent(ScreenPersonDO::getAge, reqVO.getAge())
                .eqIfPresent(ScreenPersonDO::getSex, reqVO.getSex())
                .eqIfPresent(ScreenPersonDO::getMoreType, reqVO.getMoreType())
                .likeIfPresent(ScreenPersonDO::getAddress, reqVO.getAddress())
                .eqIfPresent(ScreenPersonDO::getNation, reqVO.getNation())
                .eqIfPresent(ScreenPersonDO::getFirstType, reqVO.getFirstType())
//                .likeIfPresent(ScreenPersonDO::getScreenId, reqVO.getScreenId())
                .eqIfPresent(ScreenPersonDO::getId, reqVO.getPersonId())
                .likeIfPresent(ScreenPersonDO::getSchoolOrTemple, reqVO.getSchoolOrTemple())
                .likeIfPresent(ScreenPersonDO::getClassroom, reqVO.getClassroom())
                .eqIfPresent(ScreenPersonDO::getIsNew, reqVO.getIsNew())
                .eqIfPresent(ScreenPersonDO::getScreenType,reqVO.getScreenType())
                .likeIfPresent(ScreenPersonDO::getScreenPoint, reqVO.getScreenPoint())
                .eqIfPresent(ScreenPersonDO::getStudentType, reqVO.getStudentType())
                .betweenIfPresent(ScreenPersonDO::getScreenStartTime, reqVO.getScreenTime())
                .betweenIfPresent(ScreenPersonDO::getScreenEndTime, reqVO.getScreenTime())
                .inIfPresent(ScreenPersonDO::getDeptId, reqVO.getDeptList())
                .orderByDesc(ScreenPersonDO::getId));
    }


    // 待筛查人员分页
    default PageResult<ScreenPersonDO> selectScreenedPage(ScreenPersonPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ScreenPersonDO>()
                .likeIfPresent(ScreenPersonDO::getIdNum, reqVO.getIdNum())
                .eqIfPresent(ScreenPersonDO::getYear, reqVO.getYear())
                .likeIfPresent(ScreenPersonDO::getName, reqVO.getName())
                .eqIfPresent(ScreenPersonDO::getAge, reqVO.getAge())
                .eqIfPresent(ScreenPersonDO::getSex, reqVO.getSex())
                .likeIfPresent(ScreenPersonDO::getAddress, reqVO.getAddress())
                .eqIfPresent(ScreenPersonDO::getNation, reqVO.getNation())
                .eqIfPresent(ScreenPersonDO::getFirstType, reqVO.getFirstType())
//                .eqIfPresent(ScreenPersonDO::getScreenId, reqVO.getScreenId())
                .likeIfPresent(ScreenPersonDO::getSchoolOrTemple, reqVO.getSchoolOrTemple())
                .likeIfPresent(ScreenPersonDO::getClassroom, reqVO.getClassroom())
                .eqIfPresent(ScreenPersonDO::getIsNew, 1)
                .eqIfPresent(ScreenPersonDO::getCity, reqVO.getCity())
                .eqIfPresent(ScreenPersonDO::getTown, reqVO.getTown())
                .eqIfPresent(ScreenPersonDO::getCounty, reqVO.getCounty())
                .eqIfPresent(ScreenPersonDO::getScreenType,reqVO.getScreenType())
                .eqIfPresent(ScreenPersonDO::getIsScreened, reqVO.getIsScreened())
                .likeIfPresent(ScreenPersonDO::getScreenPoint, reqVO.getScreenPoint())
                .eqIfPresent(ScreenPersonDO::getStudentType, reqVO.getStudentType())
                .betweenIfPresent(ScreenPersonDO::getScreenStartTime, reqVO.getScreenTime())
                .betweenIfPresent(ScreenPersonDO::getScreenEndTime, reqVO.getScreenTime())
                .inIfPresent(ScreenPersonDO::getDeptId, reqVO.getDeptList())
                .orderByDesc(ScreenPersonDO::getId));
    }

    /**
     * 分页（用于同步）
     * @author ywp
     * @param reqVO
     * @return
     */
    default PageResult<ScreenPersonDO> selectPageForSynchronize(ScreenPersonPageReqVO reqVO) {
        LambdaQueryWrapperX<ScreenPersonDO> wrapper = new LambdaQueryWrapperX<ScreenPersonDO>()
                .likeIfPresent(ScreenPersonDO::getScreenPoint, reqVO.getScreenPoint())
                .eqIfPresent(ScreenPersonDO::getYear,reqVO.getYear())
                .eqIfPresent(ScreenPersonDO::getScreenType,reqVO.getScreenType())
                .eqIfPresent(ScreenPersonDO::getDeleted,0)
                .orderByDesc(ScreenPersonDO::getId);
        if(reqVO.getIdNum()!=null && reqVO.getIdNum().length()>=4){
            String idNum = reqVO.getIdNum().substring(reqVO.getIdNum().length() - 4);
            wrapper.apply("RIGHT(id_num, 4) = {0}",idNum);
        }
        return selectPage(reqVO, wrapper);
    }

    /**
     * 根据监护人手机号查询自己孩子的相关信息,默认为新生筛查
     * @param tel 手机号 string
     * @param screenType 筛查类型
     * @return StudentInfoReqVO
     */
    List<StudentInfo> selectStudentByGuardianTel(String tel,Integer screenType);


    /**
     * 根据 身份证号 查id，判断摸底表中是否有这个患者
     * @param idNum
     */
    Long isNullByIdNum(@Param("idNum") String idNum);

    /**
     * 根据 身份证号和年份 查id，判断摸底表中是否有这个患者
     * @return
     */
    Long isNullByIdNumYear(@Param("idNum") String idNum,
                           @Param("year") Integer year,
                           @Param("screenType") Integer screenType);
    /**
     * 根据 身份证号和年份 查id，判断某机构中摸底表中是否有这个患者
     * @return Long id
     */
    Long isNullByIdNumYearDeptId(@Param("idNum") String idNum,
                           @Param("year") Integer year,
                           @Param("screenType") Integer screenType,@Param("deptId") Long deptId);

    /**
     *  根据患者id，获取采集组数据
     */
    List<CollectVO> getCheckList(@Param("patientId") Long patientId,
                                 @Param("year") Integer year,
                                 @Param("screenType") Integer screenType);

    /**
     * 根据患者id，获取pdd组数据
     */
    List<ScreenTstVO> getPPDList(@Param("patientId") Long patientId,
                                 @Param("year") Integer year,
                                 @Param("screenType") Integer screenType);

    /**
     * 根据患者id，获取DR组数据
     */
    List<ChestRadiographVO> getDRList(@Param("patientId") Long patientId,
                                        @Param("year") Integer year,
                                        @Param("screenType") Integer screenType);

    /**
     * 根据患者id，获取CT组数据
     */
    List<ChestRadiographVO> getCTList(@Param("patientId") Long patientId,
                                        @Param("year") Integer year,
                                        @Param("screenType") Integer screenType);

    /**
     * 根据患者id，获取痰检组数据
     */
    List<SputumExaminationVO> getSputumList(@Param("patientId") Long patientId,
                                            @Param("year") Integer year,
                                            @Param("screenType") Integer screenType);

    /**
     * 根据患者id，获取心电图组数据
     */
    List<ElectrocardiogramVO> getElectList(@Param("patientId") Long patientId,
                                           @Param("year") Integer year,
                                           @Param("screenType") Integer screenType);

    /**
     * 根据患者id，获取诊断组数据
     */
    List<DiagnosisVO> getDiagnoList(@Param("patientId") Long patientId,
                                    @Param("year") Integer year,
                                    @Param("screenType") Integer screenType);

    /**
     * 获取图片、照片的地址
     */
    @InterceptorIgnore(tenantLine = "true")
    ImageVO getImageUrl(@Param("personId") Long personId,
                       @Param("type") Integer type,
                       @Param("screenId") String screenId,
                       @Param("screenOrder") Integer screenOrder,
                        @Param("year") Integer year,
                        @Param("screenType") Integer screenType);


    /**
     * 判断照片是否存在
     */
    @InterceptorIgnore(tenantLine = "true")
    ImageVO selectImageById(Long imageId);

    /**
     * 更新照片
     */
    @InterceptorIgnore(tenantLine = "true")
    void updateImage(@Param("imageId") Long imageId,
                     @Param("imageStr") String imageStr);

    /**
     * 获取当前工作年度的筛查编号
     */
    @InterceptorIgnore(tenantLine = "true")
    List<String> getMaxScreenId(@Param("year") Integer year,
                                @Param("town") String town);

    void updateIsScreenedById( Long personId);

    List<ScreenPersonDO> getPage(ScreenPersonPageReqVO pageReqVO);

    /**
     * 根据年份和季度，获取应查人数
     * @param year 年份
     * @param quarter 季度
     * @return 应查人集合
     */
    List<ScreenPersonDO> getScreenedCountFromPlan(@Param("year") Integer year,
                                                 @Param("quarter") Integer quarter,
                                                 @Param("screenType") Integer screenType);

    /**
     * 根据年份和季度，获取实查人数
     * @param year 年份
     * @param quarter 季度
     * @return 实查人集合
     */
    List<ScreenPersonDO> findPlannedScreening(@Param("year") Integer year,
                                              @Param("quarter") Integer quarter,
                                              @Param("screenType") Integer screenType);

    /**
     * 获取活动性肺结核集合
     * @param year 年份
     * @param quarter 季度
     * @return 活动性肺结核集合
     */
    List<ScreenPersonDO> getActiveTuberculosisList(@Param("year") Integer year,
                                                   @Param("quarter") Integer quarter,
                                                   @Param("screenType") Integer screenType);

    /**
     * 获取病原学阳性集合
     * @param year 年份
     * @param quarter 季度
     * @return 病原学阳性集合
     */
    List<ScreenPersonDO> getPathogenPositiveList(@Param("year") Integer year,
                                                 @Param("quarter") Integer quarter,
                                                 @Param("screenType") Integer screenType);

    /**
     * 确诊及治疗中患者集合
     * @param year 年份
     * @param quarter 季度
     * @return 确诊及治疗中患者集合
     */
    List<ScreenPersonDO> getConfirmedAndTreatingPatientsList(@Param("year") Integer year,
                                                             @Param("quarter") Integer quarter,
                                                             @Param("screenType") Integer screenType);

    /**
     * 潜伏感染者集合
     * @param year 年份
     * @param quarter 季度
     * @return 潜伏感染者集合
     */
    List<ScreenPersonDO> getLatentInfectionList(@Param("year") Integer year,
                                                @Param("quarter") Integer quarter,
                                                @Param("screenType") Integer screenType);

    /**
     * 获取糖尿病患者、HIV/AIDS患者集合
     * @param year 年份
     * @param quarter 季度
     * @return 获取糖尿病患者、HIV/AIDS患者集合
     */
    List<ScreenPersonDO> getDiabetesAndHivList(@Param("year") Integer year,
                                               @Param("quarter") Integer quarter,
                                               @Param("screenType") Integer screenType);

    /**
     * 统计表--统计表格--获取表格一行的数据
     * @param obj
     * @return
     */
    StatisticExportVO getByPatientInfo(PatientInfoReqVO obj);

    /**
     * 获取某筛查人员的ppd结果通知
     * @param idNum 身份证号
     * @param curDate 当前时间
     * @param screenType 筛查类型
     * @param screenOrder 筛查次序
     * @return ScreenPpdDO ppd详情
     */
    ScreenCollectDO selectPpdNotice(String idNum, Date curDate, Integer screenType, Integer screenOrder);
    /**
     * 获取某筛查人员的ppd结果通知
     * @param idNum 身份证号
     * @param curDate 当前时间
     * @param screenType 筛查类型
     * @param screenOrder 筛查次序
     * @return ScreenPpdDO ppd详情
     */
    ScreenPpdDO selectPpdDetailNotice(String idNum, Date curDate,Integer screenType,Integer screenOrder);
    /**
     * 获取某筛查人员的ct结果通知
     * @param idNum 身份证号
     * @param curDate 当前时间
     * @param screenType 筛查类型
     * @param screenOrder 筛查次序
     * @return ScreenPpdDO ppd详情
     */
    ScreenComputedTomographyDO selectCtDetailNotice(String idNum, Date curDate, Integer screenType, Integer screenOrder);
    /**
     * 获取某筛查人员的dr结果通知
     * @param idNum 身份证号
     * @param curDate 当前时间
     * @param screenType 筛查类型
     * @param screenOrder 筛查次序
     * @return ScreenPpdDO ppd详情
     */
    ScreenChestRadiographDO selectDrDetailNotice(String idNum, Date curDate, Integer screenType, Integer screenOrder);
}