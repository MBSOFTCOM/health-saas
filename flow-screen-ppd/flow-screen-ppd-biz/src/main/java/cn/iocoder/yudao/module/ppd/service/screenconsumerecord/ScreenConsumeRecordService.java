package cn.iocoder.yudao.module.ppd.service.screenconsumerecord;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.ppd.controller.admin.screenconsumerecord.vo.ScreenConsumeRecordPageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenconsumerecord.vo.ScreenConsumeRecordRespVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenconsumerecord.vo.ScreenConsumeRecordSaveReqVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenconsumerecord.ScreenConsumeRecordDO;
import jakarta.validation.Valid;

import java.util.List;

/**
 * 消耗管理记录 Service 接口
 *
 * @author 侯卿
 */
public interface ScreenConsumeRecordService {

    /**
     * 创建消耗管理记录
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createScreenConsumeRecord(@Valid ScreenConsumeRecordSaveReqVO createReqVO);

    /**
     * 更新消耗管理记录
     *
     * @param updateReqVO 更新信息
     */
    void updateScreenConsumeRecord(@Valid ScreenConsumeRecordSaveReqVO updateReqVO);

    /**
     * 删除消耗管理记录
     *
     * @param id 编号
     */
    void deleteScreenConsumeRecord(Long id);

    /**
     * 获得消耗管理记录
     *
     * @param id 编号
     * @return 消耗管理记录
     */
    ScreenConsumeRecordDO getScreenConsumeRecord(Long id);

    /**
     * 获得消耗管理记录分页
     *
     * @param pageReqVO 分页查询
     * @return 消耗管理记录分页
     */
    PageResult<ScreenConsumeRecordDO> getScreenConsumeRecordPage(ScreenConsumeRecordPageReqVO pageReqVO);

    /**
     * 获取库存记录列表
     */
    List<ScreenConsumeRecordRespVO> getScreenConsumeRecordList(Long id);
}