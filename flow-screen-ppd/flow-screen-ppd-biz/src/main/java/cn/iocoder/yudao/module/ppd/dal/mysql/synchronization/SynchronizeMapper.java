package cn.iocoder.yudao.module.ppd.dal.mysql.synchronization;


import cn.iocoder.yudao.module.ppd.controller.admin.screenchestradiograph.vo.ScreenChestRadiographSaveReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screencollect.vo.ScreenCollectSaveReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenpersonrealsituation.vo.CollectVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenpersonrealsituation.vo.ElectrocardiogramVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenpersonrealsituation.vo.ScreenPersonSaveReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenppd.vo.ScreenPpdSaveReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screensputumexamination.vo.ScreenSputumExaminationSaveReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.synchronization.vo.*;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 同步 Mapper
 *
 * @author ywp
 */
@Mapper
@InterceptorIgnore(tenantLine = "true")
public interface SynchronizeMapper {

    /**
     * 获取所有业务数据表
     * @return
     */
    List<TableSchema> getTableList(PageReqVO pageReqVO);

    /**
     * 获取业务数据表的总数
     * @return
     */
    Long getTotal(PageReqVO pageReqVO);

    //============================ 摸底表 ================================


    /**
     * 根据索引 查询摸底表记录条数
     * @param idNum
     * @param year
     */
    Long selectPersonIdByIndex(@Param("screenType") Integer screenType,@Param("year") Integer year,@Param("idNum") String idNum);

    /**
     * 更新摸底表数据
     * @param item
     */
    void updateScreenPerson(@Param("item") ScreenPersonSaveReqVO item, @Param("id")Long id);

    /**
     * 根据id查询 表中的记录条数 (可共用)
     * @param id
     * @return
     */
    Long selectCountById(@Param("tableName")String tableName,@Param("id")Long id);

    /**
     * 查询摸底表最大的 id值 (可共用)
     */
    Long selectMaxId(String tableName);

    /**
     * 根据乡镇区划、年度以及筛查类型获取最大个人编号
     */
    String selectMaxOrderNum(@Param("year") Integer year,
                                    @Param("town") String town,
                                    @Param("screenType") Integer screenType);

    /**
     * 向摸底表插入数据
     * @param item
     */
    void insertPerson(ScreenPersonSaveReqVO item);


    //============================ 采集表 ================================

    /**
     * 获取采集表分页数据
     * @param pageReqVO
     * @return
     */
    List<CollectVO> getCollectList(PageReqVO pageReqVO);
    /**
     * 采集表分页数据 条数
     * @param pageReqVO
     * @return
     */
    Long selectCollectCount(PageReqVO pageReqVO);


    /**
     * 根据索引 查询采集表id
     * @param screenId
     * @param screenOrder
     * @return
     */
    Long selectCollectIdByIndex(@Param("screenId")String screenId,@Param("screenOrder") Integer screenOrder,@Param("personId")Long personId);

    /**
     * 更新采集表数据
     */
    void updateCollect(@Param("item") ScreenCollectSaveReqVO item, @Param("id")Long id);

    /**
     * 向采集表插入数据
     * @param item
     */
    void insertCollect(ScreenCollectSaveReqVO item);

    //============================ ppd表 ================================

    /**
     * 根据索引 查询ppd表id
     * @param screenId
     * @param screenOrder
     * @return
     */
    Long selectPpdIdByIndex(@Param("screenId")String screenId,@Param("screenOrder") Integer screenOrder,@Param("personId")Long personId);

    /**
     * 更新ppd表数据
     */
    void updatePpd(@Param("item") ScreenPpdSaveReqVO item, @Param("id") Long id);

    /**
     * 向ppd表插入数据
     * @param item
     */
    void insertPpd(ScreenPpdSaveReqVO item);

    //============================ ct/dr表 ================================

    /**
     * 根据索引 查询ct/dr表id
     * @param screenId
     * @param screenOrder
     * @return
     */
    Long selectChestRadiographIdByIndex(@Param("screenId")String screenId,@Param("screenOrder") Integer screenOrder,@Param("personId")Long personId);
    /**
     * 更新ct/dr表数据
     */
    void updateChestRadiograph(@Param("item") ScreenChestRadiographSaveReqVO item, @Param("id") Long id);
    /**
     * 向ct/dr表插入数据
     * @param item
     */
    void insertChestRadiograph(ScreenChestRadiographSaveReqVO item);

    //============================ 痰检表 ================================

    /**
     * 根据索引 查询痰检表id
     * @param screenId
     * @param screenOrder
     * @return
     */
    Long selectSputumExaminationIdByIndex(@Param("screenId")String screenId,@Param("screenOrder") Integer screenOrder,@Param("personId")Long personId);
    /**
     * 更新痰检表数据
     */
    void updateSputumExamination(@Param("item") ScreenSputumExaminationSaveReqVO item, @Param("id") Long id);
    /**
     * 向痰检表插入数据
     * @param item
     */
    void insertSputumExamination(ScreenSputumExaminationSaveReqVO item);

    //============================ 心电表 ================================

    /**
     * 根据索引 查询心电表id
     * @param screenId
     * @param screenOrder
     * @return
     */
    Long selectElectrocardiogramIdByIndex(@Param("screenId")String screenId,@Param("screenOrder") Integer screenOrder,@Param("personId")Long personId);

    /**
     * 更新心电表数据
     */
    void updateElectrocardiogram(@Param("item") ElectrocardiogramVO item, @Param("id") Long id);
    /**
     * 向心电表插入数据
     * @param item
     */
    void insertElectrocardiogram(ElectrocardiogramVO item);

    //============================ 工作队伍 ================================

    /**
     * 根据用户id获取筛查点名称和筛查单位
     * @param id
     * @return
     */
    List<ScreenPointVO> getScreenPointAndAgencyByUserId(Long id);

    /**
     * 根据screenPoint获取该筛查点所有信息
     * @param screenPointId
     * @return
     */
    ScreenPointAndMemberVO getScreenPointInfoByName(Long screenPointId);


    /**
     * 根据user id 获取同一筛查点的队员信息
     * @param id
     * @return
     */
    List<UserInfoVO> getUserList(Long id);

    /**
     * 获取角色id
     * @param id
     * @return
     */
    List<String> getRoleByUserId(Long id);

    /**
     * 根据user id 获取筛查点名字
     * @param id
     * @return
     */
    String getScreenPoint(Long id);

    /**
     * 根据当前用户id 获取信息
     * @param id
     * @return
     */
    UserInfoVO getUserInfo(Long id);

    /**
     * 根据当前用户id 获取筛查单位
     * @param id
     * @return
     */
    String getScreenAgency(Long id);

    //============================ 汇总表 ================================
   /* *//**
     * 根据索引 查询汇总表id和当前已完成的分组
     * @param year
     * @param screenType
     * @param screenId
     * @param personId
     * @return
     *//*
    ScreenSumInfo selectSumIdByIndex(@Param("year")Integer year, @Param("screenType") Integer screenType, @Param("screenId") String screenId, @Param("personId") Long personId);

    *//**
     * 更新汇总表数据
     * @param item
     * @param id
     *//*
    void updateSum(@Param("item")ScreenSumSaveReqVO item,@Param("id") Long id);

    *//**
     * 插入数据到汇总表
     * @param item
     *//*
    void insertSum(ScreenSumSaveReqVO item);*/
}
