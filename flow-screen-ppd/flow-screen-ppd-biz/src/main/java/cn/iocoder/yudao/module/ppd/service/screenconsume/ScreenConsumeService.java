package cn.iocoder.yudao.module.ppd.service.screenconsume;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.ppd.controller.admin.screenconsume.vo.ScreenConsumePageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenconsume.vo.ScreenConsumeSaveReqVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenconsume.ScreenConsumeDO;
import jakarta.validation.Valid;

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
     * 增加入库量、当前库存
     */
    Boolean increaseScreenConsume(Long id, Integer number);

    /**
     * 减少当前库存
     */
    Boolean decreaseScreenConsume(Long id, Integer number);
}