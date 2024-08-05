package cn.iocoder.yudao.module.ppd.service.synchronization;


import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.ppd.controller.admin.screenchestradiograph.vo.ScreenChestRadiographPageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenchestradiograph.vo.ScreenChestRadiographSaveReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screencollect.vo.ScreenCollectPageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screencollect.vo.ScreenCollectSaveReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenpersonrealsituation.vo.ScreenPersonPageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenpersonrealsituation.vo.ScreenPersonSaveReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenppd.vo.ScreenPpdPageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenppd.vo.ScreenPpdSaveReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.synchronization.vo.PageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.synchronization.vo.TableSchema;
import cn.iocoder.yudao.module.ppd.controller.admin.synchronization.vo.UserLoginInfoVO;
import cn.iocoder.yudao.module.ppd.controller.admin.synchronization.vo.WorkTeamVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenchestradiograph.ScreenChestRadiographDO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screencollect.ScreenCollectDO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenpersonrealsituation.ScreenPersonDO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenppd.ScreenPpdDO;

import java.util.List;

/**
 * 同步 Service 接口
 *
 * @author ywp
 */
public interface SynchronizeService {
    PageResult<TableSchema> getTableList(PageReqVO pageReqVO);

    /**
     * 获取摸底表待筛查人员数据
     * @param pageReqVO
     * @return
     */
    PageResult<ScreenPersonDO> getScreenPersonPage(ScreenPersonPageReqVO pageReqVO);

    /**
     * 更新摸底表数据
     * @param list
     */
    void updateScreenPerson(List<ScreenPersonSaveReqVO> list);

    /**
     * 获取采集表分页数据
     * @param pageReqVO
     * @return
     */
    PageResult<ScreenCollectDO> getCollectPage(ScreenCollectPageReqVO pageReqVO);

    /**
     * 更新采集表数据
     * @param list
     */
    void updateCollect(List<ScreenCollectSaveReqVO> list);

    /**
     * 获取ppd分页数据
     * @param pageReqVO
     * @return
     */
    PageResult<ScreenPpdDO> getPpdPage(ScreenPpdPageReqVO pageReqVO);

    /**
     * 更新ppd表数据
     * @param list
     */
    void updatePpd(List<ScreenPpdSaveReqVO> list);

    /**
     * 获取dr/ct分页数据
     * @param pageReqVO
     * @return
     */
    PageResult<ScreenChestRadiographDO> getChestRadiographPage(ScreenChestRadiographPageReqVO pageReqVO);

    /**
     * 更新dr/ct表数据
     * @param list
     */
    void updateChestRadiograph(List<ScreenChestRadiographSaveReqVO> list);


    /**
     * 获取工作队伍信息
     * @param screenPointId
     * @return
     */
    List<WorkTeamVO> getWorkTeam(Long screenPointId);

    /**
     * 获取当前登录用户信息
     * @param id
     * @return
     */
    UserLoginInfoVO getUserInfo(Long id);


/*    *//**
     * 获取汇总表数据
     * @param pageReqVO
     * @return
     *//*
    List<ScreenSumDO> getSumPage(ScreenSumPageReqVO pageReqVO);

    *//**
     * 更新汇总表数据
     * @param list
     *//*
    void updateSum(List<ScreenSumSaveReqVO> list);*/
}
