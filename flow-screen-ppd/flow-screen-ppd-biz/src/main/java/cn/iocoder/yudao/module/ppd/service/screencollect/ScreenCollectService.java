package cn.iocoder.yudao.module.ppd.service.screencollect;


import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.ppd.controller.admin.screencollect.vo.ScreenCollectPageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screencollect.vo.ScreenCollectSaveReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screencollect.vo.WxCollectReqVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screencollect.ScreenCollectDO;
import jakarta.validation.Valid;

/**
 * 采集 Service 接口
 *
 * @author 芋道源码
 */
public interface ScreenCollectService {

    /**
     * 创建采集
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createScreenCollect(@Valid ScreenCollectSaveReqVO createReqVO);
    /**
     * 微信小程序创建采集
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createScreenCollect(@Valid WxCollectReqVO createReqVO);

    Integer getLastOrder(Integer screenType,Integer year,Long personId);
    /**
     * 更新采集
     *
     * @param updateReqVO 更新信息
     */
    void updateScreenCollect(@Valid ScreenCollectSaveReqVO updateReqVO);

    /**
     * 删除采集
     *
     * @param id 编号
     */
    void deleteScreenCollect(Long id);

    /**
     * 获得采集
     *
     * @param id 编号
     * @return 采集
     */
    ScreenCollectDO getScreenCollect(Long id);

    /**
     * 获得采集分页
     *
     * @param pageReqVO 分页查询
     * @return 采集分页
     */
    PageResult<ScreenCollectDO> getScreenCollectPage(ScreenCollectPageReqVO pageReqVO);

}