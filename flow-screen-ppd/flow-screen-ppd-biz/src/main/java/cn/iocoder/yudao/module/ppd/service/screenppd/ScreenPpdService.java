package cn.iocoder.yudao.module.ppd.service.screenppd;


import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.ppd.controller.admin.screenppd.vo.ScreenPpdPageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenppd.vo.ScreenPpdSaveReqVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenppd.ScreenPpdDO;
import jakarta.validation.Valid;

/**
 * ppd组记录 Service 接口
 *
 * @author 芋道源码
 */
public interface ScreenPpdService {

    /**
     * 创建ppd组记录
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createScreenPpd(@Valid ScreenPpdSaveReqVO createReqVO);

    /**
     * 更新ppd组记录
     *
     * @param updateReqVO 更新信息
     */
    void updateScreenPpd(@Valid ScreenPpdSaveReqVO updateReqVO);

    /**
     * 删除ppd组记录
     *
     * @param id 编号
     */
    void deleteScreenPpd(Long id);

    /**
     * 获得ppd组记录
     *
     * @param id 编号
     * @return ppd组记录
     */
    ScreenPpdDO getScreenPpd(Long id);

    /**
     * 获得ppd组记录分页
     *
     * @param pageReqVO 分页查询
     * @return ppd组记录分页
     */
    PageResult<ScreenPpdDO> getScreenPpdPage(ScreenPpdPageReqVO pageReqVO);

}