package cn.iocoder.yudao.module.ppd.service.screenreagent;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.ppd.controller.admin.screenreagent.vo.ScreenReagentPageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenreagent.vo.ScreenReagentSaveReqVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenreagent.ScreenReagentDO;
import cn.iocoder.yudao.module.ppd.dal.mysql.screenreagent.ScreenReagentMapper;
import cn.iocoder.yudao.module.ppd.service.screenreagent.ScreenReagentService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.cd.enums.ErrorCodeConstants.SCREEN_REAGENT_NOT_EXISTS;

/**
 * 试剂 Service 实现类
 *
 * @author 侯卿
 */
@Service
@Validated
public class ScreenReagentServiceImpl implements ScreenReagentService {

    @Resource
    private ScreenReagentMapper screenReagentMapper;

    @Override
    public Long createScreenReagent(ScreenReagentSaveReqVO createReqVO) {
        // 插入
        ScreenReagentDO screenReagent = BeanUtils.toBean(createReqVO, ScreenReagentDO.class);
        screenReagentMapper.insert(screenReagent);
        // 返回
        return screenReagent.getId();
    }

    @Override
    public void updateScreenReagent(ScreenReagentSaveReqVO updateReqVO) {
        // 校验存在
        validateScreenReagentExists(updateReqVO.getId());
        // 更新
        ScreenReagentDO updateObj = BeanUtils.toBean(updateReqVO, ScreenReagentDO.class);
        screenReagentMapper.updateById(updateObj);
    }

    @Override
    public void deleteScreenReagent(Long id) {
        // 校验存在
        validateScreenReagentExists(id);
        // 删除
        screenReagentMapper.deleteById(id);
    }

    private void validateScreenReagentExists(Long id) {
        if (screenReagentMapper.selectById(id) == null) {
            throw exception(SCREEN_REAGENT_NOT_EXISTS);
        }
    }

    @Override
    public ScreenReagentDO getScreenReagent(Long id) {
        return screenReagentMapper.selectById(id);
    }

    @Override
    public PageResult<ScreenReagentDO> getScreenReagentPage(ScreenReagentPageReqVO pageReqVO) {
        return screenReagentMapper.selectPage(pageReqVO);
    }

}