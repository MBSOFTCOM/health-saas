package cn.iocoder.yudao.module.childhealth.service.caseType;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.caseType.vo.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.caseType.CaseTypeConfigDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.caseType.CaseTypeConfigMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.*;

/**
 * 专案类型配置 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class CaseTypeConfigServiceImpl implements CaseTypeConfigService {

    @Resource
    private CaseTypeConfigMapper caseTypeConfigMapper;

    @Override
    public Long createCaseTypeConfig(CaseTypeConfigSaveReqVO createReqVO) {
        CaseTypeConfigDO caseTypeConfig = BeanUtils.toBean(createReqVO, CaseTypeConfigDO.class);
        caseTypeConfigMapper.insert(caseTypeConfig);
        return caseTypeConfig.getId();
    }

    @Override
    public void updateCaseTypeConfig(CaseTypeConfigSaveReqVO updateReqVO) {
        // 校验存在
        validateCaseTypeConfigExists(updateReqVO.getId());
        // 更新
        CaseTypeConfigDO updateObj = BeanUtils.toBean(updateReqVO, CaseTypeConfigDO.class);
        caseTypeConfigMapper.updateById(updateObj);
    }

    @Override
    public void deleteCaseTypeConfig(Long id) {
        // 校验存在
        validateCaseTypeConfigExists(id);
        // 删除
        caseTypeConfigMapper.deleteById(id);
    }

    private void validateCaseTypeConfigExists(Long id) {
        if (caseTypeConfigMapper.selectById(id) == null) {
            throw exception(CASE_TYPE_CONFIG_NOT_EXISTS);
        }
    }

    @Override
    public CaseTypeConfigDO getCaseTypeConfig(Long id) {
        return caseTypeConfigMapper.selectById(id);
    }

    @Override
    public PageResult<CaseTypeConfigDO> getCaseTypeConfigPage(CaseTypeConfigPageReqVO pageReqVO) {
        return caseTypeConfigMapper.selectPage(pageReqVO);
    }

    @Override
    public List<CaseTypeConfigDO> getCaseTypeConfigList() {
        return caseTypeConfigMapper.selectList();
    }

}