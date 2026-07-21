package cn.iocoder.yudao.module.childhealth.service.dashboard;

import cn.iocoder.yudao.module.childhealth.controller.admin.dashboard.vo.*;

import java.util.List;

/**
 * 数据看板 Service 接口
 *
 * 创建日期: 2026-07-20
 * 模块: 1. 数据看板 + 12. 运营工作台
 */
public interface DashboardService {

    /**
     * 看板总览：参检率/阳性率/复筛率/随访率
     * 优先读 ops_indicator_snapshot 最新一条；缺失则实时聚合
     */
    DashboardOverviewRespVO getOverview(DashboardQueryReqVO reqVO);

    /**
     * 趋势图：按日期展示参检率/阳性率/复筛率/随访率
     * 数据源：ops_indicator_snapshot 按日期查询
     */
    List<DashboardTrendRespVO> getTrend(DashboardQueryReqVO reqVO);

    /**
     * 按区域聚合统计
     */
    List<DashboardCategoryRespVO> getByRegion(DashboardQueryReqVO reqVO);

    /**
     * 按学校聚合统计
     */
    List<DashboardCategoryRespVO> getBySchool(DashboardQueryReqVO reqVO);

    /**
     * 按五健专项聚合统计（眼/口腔/骨骼/心理/体形）
     */
    List<DashboardCategoryRespVO> getByCategory(DashboardQueryReqVO reqVO);

}
