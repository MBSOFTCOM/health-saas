package cn.iocoder.yudao.module.ppd.service.screenppd;


import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.ppd.controller.admin.screenppd.vo.ScreenPpdPageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenppd.vo.ScreenPpdSaveReqVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenppd.ScreenPpdDO;
import cn.iocoder.yudao.module.ppd.dal.mysql.screenppd.ScreenPpdMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.cd.enums.ErrorCodeConstants.SCREEN_PPD_NOT_EXISTS;

/**
 * ppd组记录 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ScreenPpdServiceImpl implements ScreenPpdService {

    @Resource
    private ScreenPpdMapper screenPpdMapper;

    @Override
    public Long createScreenPpd(ScreenPpdSaveReqVO createReqVO) {
        // 插入
        ScreenPpdDO screenPpd = BeanUtils.toBean(createReqVO, ScreenPpdDO.class);
        screenPpdMapper.insert(screenPpd);
        // 返回
        return screenPpd.getId();
    }

    @Override
    public void updateScreenPpd(ScreenPpdSaveReqVO updateReqVO) {
        // 校验存在
        validateScreenPpdExists(updateReqVO.getId());
        // 更新
        ScreenPpdDO updateObj = BeanUtils.toBean(updateReqVO, ScreenPpdDO.class);
        screenPpdMapper.updateById(updateObj);
    }

    @Override
    public void deleteScreenPpd(Long id) {
        // 校验存在
        validateScreenPpdExists(id);
        // 删除
        screenPpdMapper.deleteById(id);
    }

    private void validateScreenPpdExists(Long id) {
        if (screenPpdMapper.selectById(id) == null) {
            throw exception(SCREEN_PPD_NOT_EXISTS);
        }
    }

    @Override
    public ScreenPpdDO getScreenPpd(Long id) {
        return screenPpdMapper.selectById(id);
    }

    @Override
    public PageResult<ScreenPpdDO> getScreenPpdPage(ScreenPpdPageReqVO pageReqVO) {
        return screenPpdMapper.selectPage(pageReqVO);
    }

}