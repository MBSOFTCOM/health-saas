package cn.iocoder.yudao.module.ppd.service.screencollect;


import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.ppd.controller.admin.screencollect.vo.ScreenCollectPageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screencollect.vo.ScreenCollectSaveReqVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screencollect.ScreenCollectDO;
import cn.iocoder.yudao.module.ppd.dal.mysql.screencollect.ScreenCollectMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.cd.enums.ErrorCodeConstants.SCREEN_COLLECT_NOT_EXISTS;

/**
 * 采集 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ScreenCollectServiceImpl implements ScreenCollectService {

    @Resource
    private ScreenCollectMapper screenCollectMapper;

    @Override
    public Long createScreenCollect(ScreenCollectSaveReqVO createReqVO) {
        // 插入
        ScreenCollectDO screenCollect = BeanUtils.toBean(createReqVO, ScreenCollectDO.class);
        screenCollectMapper.insert(screenCollect);
        // 返回
        return screenCollect.getId();
    }

    @Override
    public void updateScreenCollect(ScreenCollectSaveReqVO updateReqVO) {
        // 校验存在
        validateScreenCollectExists(updateReqVO.getId());
        // 更新
        ScreenCollectDO updateObj = BeanUtils.toBean(updateReqVO, ScreenCollectDO.class);
        screenCollectMapper.updateById(updateObj);
    }

    @Override
    public void deleteScreenCollect(Long id) {
        // 校验存在
        validateScreenCollectExists(id);
        // 删除
        screenCollectMapper.deleteById(id);
    }

    private void validateScreenCollectExists(Long id) {
        if (screenCollectMapper.selectById(id) == null) {
            throw exception(SCREEN_COLLECT_NOT_EXISTS);
        }
    }

    @Override
    public ScreenCollectDO getScreenCollect(Long id) {
        return screenCollectMapper.selectById(id);
    }

    @Override
    public PageResult<ScreenCollectDO> getScreenCollectPage(ScreenCollectPageReqVO pageReqVO) {
        return screenCollectMapper.selectPage(pageReqVO);
    }

}