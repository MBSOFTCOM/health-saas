package cn.iocoder.yudao.module.childhealth.service.screening;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.item.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.ScreeningItemConfigDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.screening.ScreeningItemConfigMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.*;

/**
 * 筛查项目配置 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ScreeningItemConfigServiceImpl implements ScreeningItemConfigService {

    @Resource
    private ScreeningItemConfigMapper screeningItemConfigMapper;

    @Override
    public Long createScreeningItemConfig(ScreeningItemConfigSaveReqVO createReqVO) {
        // 校验项目编码唯一
        validateItemCodeUnique(null, createReqVO.getItemCode());
        
        ScreeningItemConfigDO screeningItemConfig = BeanUtils.toBean(createReqVO, ScreeningItemConfigDO.class);
        screeningItemConfigMapper.insert(screeningItemConfig);
        return screeningItemConfig.getId();
    }

    @Override
    public void updateScreeningItemConfig(ScreeningItemConfigSaveReqVO updateReqVO) {
        validateScreeningItemConfigExists(updateReqVO.getId());
        validateItemCodeUnique(updateReqVO.getId(), updateReqVO.getItemCode());
        
        ScreeningItemConfigDO updateObj = BeanUtils.toBean(updateReqVO, ScreeningItemConfigDO.class);
        screeningItemConfigMapper.updateById(updateObj);
    }

    @Override
    public void deleteScreeningItemConfig(Long id) {
        validateScreeningItemConfigExists(id);
        screeningItemConfigMapper.deleteById(id);
    }

    private void validateScreeningItemConfigExists(Long id) {
        if (screeningItemConfigMapper.selectById(id) == null) {
            throw exception(SCREENING_ITEM_NOT_EXISTS);
        }
    }

    private void validateItemCodeUnique(Long id, String itemCode) {
        ScreeningItemConfigDO config = screeningItemConfigMapper.selectByItemCode(itemCode);
        if (config == null) {
            return;
        }
        if (id == null) {
            throw exception(SCREENING_ITEM_CODE_DUPLICATE);
        }
        if (!config.getId().equals(id)) {
            throw exception(SCREENING_ITEM_CODE_DUPLICATE);
        }
    }

    @Override
    public ScreeningItemConfigDO getScreeningItemConfig(Long id) {
        return screeningItemConfigMapper.selectById(id);
    }

    @Override
    public PageResult<ScreeningItemConfigDO> getScreeningItemConfigPage(ScreeningItemConfigPageReqVO pageReqVO) {
        return screeningItemConfigMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ScreeningItemConfigDO> getScreeningItemConfigList(ScreeningItemConfigListReqVO listReqVO) {
        return screeningItemConfigMapper.selectList(listReqVO);
    }

    @Override
    public List<ScreeningItemConfigDO> getByCategory(String category) {
        return screeningItemConfigMapper.selectByCategory(category);
    }

    @Override
    public List<ScreeningItemConfigDO> getActiveList() {
        return screeningItemConfigMapper.selectActiveList();
    }

}