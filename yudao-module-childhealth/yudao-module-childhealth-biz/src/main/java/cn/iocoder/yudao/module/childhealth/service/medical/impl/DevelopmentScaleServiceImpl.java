package cn.iocoder.yudao.module.childhealth.service.medical.impl;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.medical.DevelopmentScaleDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.medical.DevelopmentScaleMapper;
import cn.iocoder.yudao.module.childhealth.service.medical.DevelopmentScaleService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.DEVELOPMENT_SCALE_CODE_DUPLICATE;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.DEVELOPMENT_SCALE_NOT_EXISTS;

/**
 * 发育评估量表定义表 Service 实现类
 *
 * 模块: A. 儿童基础健康检查（A6-发育评估量表定义表，18+套）
 * 创建日期: 2026-07-20
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class DevelopmentScaleServiceImpl implements DevelopmentScaleService {

    @Resource
    private DevelopmentScaleMapper developmentScaleMapper;

    @Override
    public Long createDevelopmentScale(Object saveReqVO) {
        // TODO 后续替换为 DevelopmentScaleSaveReqVO
        DevelopmentScaleDO scale = BeanUtils.toBean(saveReqVO, DevelopmentScaleDO.class);
        // 编码唯一性校验
        if (scale.getScaleCode() != null
                && developmentScaleMapper.selectByCode(scale.getScaleCode()) != null) {
            throw exception(DEVELOPMENT_SCALE_CODE_DUPLICATE);
        }
        developmentScaleMapper.insert(scale);
        return scale.getId();
    }

    @Override
    public void updateDevelopmentScale(Object saveReqVO) {
        // TODO 后续替换为 DevelopmentScaleSaveReqVO
        DevelopmentScaleDO updateObj = BeanUtils.toBean(saveReqVO, DevelopmentScaleDO.class);
        validateDevelopmentScaleExists(updateObj.getId());
        developmentScaleMapper.updateById(updateObj);
    }

    @Override
    public void deleteDevelopmentScale(Long id) {
        validateDevelopmentScaleExists(id);
        developmentScaleMapper.deleteById(id);
    }

    @Override
    public DevelopmentScaleDO getDevelopmentScale(Long id) {
        return developmentScaleMapper.selectById(id);
    }

    @Override
    public PageResult<DevelopmentScaleDO> getDevelopmentScalePage(PageParam pageParam) {
        // TODO 后续替换为 DevelopmentScalePageReqVO，并增加查询条件
        return developmentScaleMapper.selectPage(pageParam, null);
    }

    @Override
    public DevelopmentScaleDO selectByCode(String scaleCode) {
        return developmentScaleMapper.selectByCode(scaleCode);
    }

    @Override
    public List<DevelopmentScaleDO> selectActiveList() {
        return developmentScaleMapper.selectActiveList();
    }

    private void validateDevelopmentScaleExists(Long id) {
        if (id == null || developmentScaleMapper.selectById(id) == null) {
            throw exception(DEVELOPMENT_SCALE_NOT_EXISTS);
        }
    }

}
