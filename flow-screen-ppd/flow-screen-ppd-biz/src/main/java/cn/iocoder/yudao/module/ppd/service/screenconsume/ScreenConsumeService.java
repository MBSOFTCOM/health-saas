package cn.iocoder.yudao.module.ppd.service.screenconsume;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.ppd.controller.admin.screenconsume.vo.ScreenConsumeImportVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenconsume.vo.ScreenConsumePageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenconsume.vo.ScreenConsumeSaveReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenconsume.vo.ScreenConsumeStatisticsRespVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenreagent.vo.ScreenReagentImportRespVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenconsume.ScreenConsumeDO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenconsumerecord.ScreenConsumeRecordDO;
import jakarta.validation.Valid;

import java.util.List;

/**
 * 消耗管理 Service 接口
 *
 * @author 侯卿
 */
public interface ScreenConsumeService {

    /**
     * 创建消耗管理
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createScreenConsume(@Valid ScreenConsumeSaveReqVO createReqVO);

    /**
     * 更新消耗管理
     *
     * @param updateReqVO 更新信息
     */
    void updateScreenConsume(@Valid ScreenConsumeSaveReqVO updateReqVO);

    /**
     * 删除消耗管理
     *
     * @param id 编号
     */
    void deleteScreenConsume(Long id);

    /**
     * 获得消耗管理
     *
     * @param id 编号
     * @return 消耗管理
     */
    ScreenConsumeDO getScreenConsume(Long id);

    /**
     * 获得消耗管理分页
     *
     * @param pageReqVO 分页查询
     * @return 消耗管理分页
     */
    PageResult<ScreenConsumeDO> getScreenConsumePage(ScreenConsumePageReqVO pageReqVO);
    /**
     * 获得可使用的试剂批号，库存数大于0
     *
     * @param pageReqVO ScreenConsumePageReqVO
     * @return List<ScreenConsumeDO>
     */
    List<ScreenConsumeDO> getUsableScreenConsume(ScreenConsumePageReqVO pageReqVO);

    /**
     * 增加入库量、当前库存
     */
    Boolean increaseScreenConsume(Long id, Integer number);

    /**
     * 减少当前库存
     */
    Boolean decreaseScreenConsume(Long id, Integer number);

    List<ScreenConsumeImportVO> createSampleData();

    /**
     * 导入消耗管理
     */
    ScreenReagentImportRespVO importScreenConsume(List<ScreenConsumeImportVO> list);

    /**
     * 消耗量管理数据统计
     */
    List<ScreenConsumeStatisticsRespVO> getScreenConsumeStatistics(ScreenConsumePageReqVO pageReqVO);
    /**
     * 上传数据时，批量减少当前库存
     */
    Integer decreaseScreenConsumeBatch(List<ScreenConsumeRecordDO> list);

    /**
     *
     * @param reagentId 试剂id
     * @param deptId 部门id
     * @return Integer 某机构的一种试剂的总数
     */
    Integer getReagentTotalNum(Long reagentId,Long deptId);
}