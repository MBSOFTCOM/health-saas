package cn.iocoder.yudao.module.ppd.service.screenstaticshistory;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.ppd.controller.admin.screenstaticshistory.vo.ScreenStaticsHistoryPageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenstaticshistory.vo.ScreenStaticsHistoryRespVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenstaticshistory.vo.ScreenStaticsHistorySaveReqVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenstaticshistory.ScreenStaticsHistoryDO;
import jakarta.validation.Valid;

/**
 * 工作进展报告-统计表-导出的历史选项 Service 接口
 *
 * @author 福乐云
 */
public interface ScreenStaticsHistoryService {

    /**
     * 创建工作进展报告-统计表-导出的历史选项
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createScreenStaticsHistory(@Valid ScreenStaticsHistorySaveReqVO createReqVO);
    /**
     * 覆盖工作进展报告-统计表-导出的历史选项
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long recoverScreenStaticsHistory(@Valid ScreenStaticsHistorySaveReqVO createReqVO);

    /**
     * 更新工作进展报告-统计表-导出的历史选项
     *
     * @param updateReqVO 更新信息
     */
    void updateScreenStaticsHistory(@Valid ScreenStaticsHistorySaveReqVO updateReqVO);

    /**
     * 删除工作进展报告-统计表-导出的历史选项
     *
     * @param id 编号
     */
    void deleteScreenStaticsHistory(Long id);

    /**
     * 获得工作进展报告-统计表-导出的历史选项
     *
     * @param id 编号
     * @return 工作进展报告-统计表-导出的历史选项
     */
    ScreenStaticsHistoryDO getScreenStaticsHistory(Long id);

    /**
     * 获得工作进展报告-统计表-导出的历史选项分页
     *
     * @param pageReqVO 分页查询
     * @return 工作进展报告-统计表-导出的历史选项分页
     */
    PageResult<ScreenStaticsHistoryDO> getScreenStaticsHistoryPage(ScreenStaticsHistoryPageReqVO pageReqVO);

    ScreenStaticsHistoryRespVO getHistory(Long deptId);

}