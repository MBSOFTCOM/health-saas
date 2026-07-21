package cn.iocoder.yudao.module.childhealth.service.casemanagement.impl;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.casemanagement.CaseRecoveryStandardDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.casemanagement.CaseRecoveryStandardMapper;
import cn.iocoder.yudao.module.childhealth.service.casemanagement.CaseRecoveryStandardService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.*;

/**
 * 专案康复达标标准 Service 实现类
 *
 * 模块: B.专案管理
 * 创建日期: 2026-07-20
 */
@Service
@Validated
public class CaseRecoveryStandardServiceImpl implements CaseRecoveryStandardService {

    @Resource
    private CaseRecoveryStandardMapper caseRecoveryStandardMapper;

    @Override
    public Long createCaseRecoveryStandard(Object createReqVO) {
        // TODO 后续替换为 CaseRecoveryStandardSaveReqVO
        CaseRecoveryStandardDO caseRecoveryStandard = BeanUtils.toBean(createReqVO, CaseRecoveryStandardDO.class);
        caseRecoveryStandardMapper.insert(caseRecoveryStandard);
        return caseRecoveryStandard.getId();
    }

    @Override
    public void updateCaseRecoveryStandard(Object updateReqVO) {
        // TODO 后续替换为 CaseRecoveryStandardSaveReqVO，并校验存在
        // validateCaseRecoveryStandardExists(updateReqVO.getId());
        CaseRecoveryStandardDO updateObj = BeanUtils.toBean(updateReqVO, CaseRecoveryStandardDO.class);
        caseRecoveryStandardMapper.updateById(updateObj);
    }

    @Override
    public void deleteCaseRecoveryStandard(Long id) {
        validateCaseRecoveryStandardExists(id);
        caseRecoveryStandardMapper.deleteById(id);
    }

    private void validateCaseRecoveryStandardExists(Long id) {
        if (caseRecoveryStandardMapper.selectById(id) == null) {
            throw exception(CASE_RECOVERY_STANDARD_NOT_EXISTS);
        }
    }

    @Override
    public CaseRecoveryStandardDO getCaseRecoveryStandard(Long id) {
        return caseRecoveryStandardMapper.selectById(id);
    }

    @Override
    public PageResult<CaseRecoveryStandardDO> getCaseRecoveryStandardPage(PageParam pageParam) {
        // TODO 后续替换为 CaseRecoveryStandardPageReqVO，并实现分页查询条件
        return caseRecoveryStandardMapper.selectPage(pageParam, null);
    }

    @Override
    public List<CaseRecoveryStandardDO> selectListByCaseType(Integer caseType) {
        return caseRecoveryStandardMapper.selectListByCaseType(caseType);
    }

    @Override
    public boolean checkRecovery(Long caseId, Object indicatorValues) {
        // TODO 业务方法：康复达标判断
        //  1. 根据 caseId 查询专案，获取 caseType 与 caseSubtype
        //  2. 调用 selectListByCaseType 获取该类型所有达标标准
        //  3. 遍历标准，根据 standardOperator（>=, <=, ==, >, <）与 standardValue 对比指标值
        //  4. 全部指标达标返回 true，否则返回 false
        //  5. 可结合 CaseManagementService.closeCase 触发结案归档
        return false;
    }

}
