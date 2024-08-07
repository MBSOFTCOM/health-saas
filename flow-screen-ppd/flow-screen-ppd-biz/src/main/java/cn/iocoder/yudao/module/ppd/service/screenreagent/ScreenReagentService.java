package cn.iocoder.yudao.module.ppd.service.screenreagent;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.ppd.controller.admin.screenreagent.vo.ScreenReagentPageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenreagent.vo.ScreenReagentSaveReqVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenreagent.ScreenReagentDO;
import jakarta.validation.Valid;

/**
 * 试剂 Service 接口
 *
 * @author 侯卿
 */
public interface ScreenReagentService {

    /**
     * 创建试剂
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createScreenReagent(@Valid ScreenReagentSaveReqVO createReqVO);

    /**
     * 更新试剂
     *
     * @param updateReqVO 更新信息
     */
    void updateScreenReagent(@Valid ScreenReagentSaveReqVO updateReqVO);

    /**
     * 删除试剂
     *
     * @param id 编号
     */
    void deleteScreenReagent(Long id);

    /**
     * 获得试剂
     *
     * @param id 编号
     * @return 试剂
     */
    ScreenReagentDO getScreenReagent(Long id);

    /**
     * 获得试剂分页
     *
     * @param pageReqVO 分页查询
     * @return 试剂分页
     */
    PageResult<ScreenReagentDO> getScreenReagentPage(ScreenReagentPageReqVO pageReqVO);

}