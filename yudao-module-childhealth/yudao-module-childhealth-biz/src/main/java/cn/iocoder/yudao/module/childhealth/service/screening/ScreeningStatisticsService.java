package cn.iocoder.yudao.module.childhealth.service.screening;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.statistics.ScreeningStatisticsPageReqVO;
import cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.statistics.ScreeningStatisticsSaveReqVO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.ScreeningStatisticsDO;
import jakarta.validation.Valid;

import java.util.List;

/**
 * 筛查统计 Service 接口
 *
 * @author 芋道源码
 */
public interface ScreeningStatisticsService {

    /**
     * 创建筛查统计
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createScreeningStatistics(@Valid ScreeningStatisticsSaveReqVO createReqVO);

    /**
     * 更新筛查统计
     *
     * @param updateReqVO 更新信息
     */
    void updateScreeningStatistics(@Valid ScreeningStatisticsSaveReqVO updateReqVO);

    /**
     * 删除筛查统计
     *
     * @param id 编号
     */
    void deleteScreeningStatistics(Long id);

    /**
     * 获得筛查统计
     *
     * @param id 编号
     * @return 筛查统计
     */
    ScreeningStatisticsDO getScreeningStatistics(Long id);

    /**
     * 获得筛查统计分页
     *
     * @param pageReqVO 分页查询
     * @return 筛查统计分页
     */
    PageResult<ScreeningStatisticsDO> getScreeningStatisticsPage(ScreeningStatisticsPageReqVO pageReqVO);

    /**
     * 获得筛查统计列表（根据批次ID）
     *
     * @param batchId 批次ID
     * @return 筛查统计列表
     */
    List<ScreeningStatisticsDO> getScreeningStatisticsListByBatchId(Long batchId);

    /**
     * 获得筛查统计列表（根据学校ID）
     *
     * @param schoolId 学校ID
     * @return 筛查统计列表
     */
    List<ScreeningStatisticsDO> getScreeningStatisticsListBySchoolId(Long schoolId);

}