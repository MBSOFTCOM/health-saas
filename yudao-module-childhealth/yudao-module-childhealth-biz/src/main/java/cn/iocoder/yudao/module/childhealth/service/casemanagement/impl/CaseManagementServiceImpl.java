package cn.iocoder.yudao.module.childhealth.service.casemanagement.impl;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.casemanagement.CaseManagementDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.casemanagement.CaseManagementMapper;
import cn.iocoder.yudao.module.childhealth.service.casemanagement.CaseManagementService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.*;

/**
 * 专案主表 Service 实现类
 *
 * 模块: B.专案管理
 * 创建日期: 2026-07-20
 */
@Service
@Validated
public class CaseManagementServiceImpl implements CaseManagementService {

    @Resource
    private CaseManagementMapper caseManagementMapper;

    @Override
    public Long createCaseManagement(Object createReqVO) {
        // TODO 后续替换为 CaseManagementSaveReqVO，并校验 caseNo 唯一
        CaseManagementDO caseManagement = BeanUtils.toBean(createReqVO, CaseManagementDO.class);
        caseManagementMapper.insert(caseManagement);
        return caseManagement.getId();
    }

    @Override
    public void updateCaseManagement(Object updateReqVO) {
        // TODO 后续替换为 CaseManagementSaveReqVO，并校验存在与 caseNo 唯一
        // validateCaseManagementExists(updateReqVO.getId());
        CaseManagementDO updateObj = BeanUtils.toBean(updateReqVO, CaseManagementDO.class);
        caseManagementMapper.updateById(updateObj);
    }

    @Override
    public void deleteCaseManagement(Long id) {
        validateCaseManagementExists(id);
        caseManagementMapper.deleteById(id);
    }

    private void validateCaseManagementExists(Long id) {
        if (caseManagementMapper.selectById(id) == null) {
            throw exception(CASE_MANAGEMENT_NOT_EXISTS);
        }
    }

    @Override
    public CaseManagementDO getCaseManagement(Long id) {
        return caseManagementMapper.selectById(id);
    }

    @Override
    public PageResult<CaseManagementDO> getCaseManagementPage(PageParam pageParam) {
        // TODO 后续替换为 CaseManagementPageReqVO，并实现分页查询条件
        return caseManagementMapper.selectPage(pageParam, null);
    }

    @Override
    public Long autoCreateCaseFromAbnormal(Object abnormalRecord) {
        // TODO 业务方法：异常自动建专案
        //  1. 根据异常类型映射专案类型（如：营养不良异常 -> 营养不良专案）
        //  2. 校验该儿童是否已存在同类型进行中专案，避免重复建立
        //  3. 生成专案编号（按规则生成，如 CASE+yyyyMMdd+seq）
        //  4. 自动填充专案等级、责任医生等字段
        //  5. 写入专案表，并回写来源记录的专案ID
        CaseManagementDO caseManagement = BeanUtils.toBean(abnormalRecord, CaseManagementDO.class);
        caseManagement.setCaseSource(0); // 0自动
        caseManagement.setStatus(0); // 0进行中
        caseManagement.setEstablishDate(LocalDate.now());
        caseManagementMapper.insert(caseManagement);
        return caseManagement.getId();
    }

    @Override
    public String generateCaseCard(Long caseId) {
        // TODO 业务方法：生成个案卡
        //  1. 查询专案基本信息、儿童信息、所有随访记录
        //  2. 调用报告生成服务渲染个案卡模板（PDF/Word）
        //  3. 上传至文件存储，获取访问URL
        //  4. 回写 case_management.case_card_url 字段
        validateCaseManagementExists(caseId);
        CaseManagementDO updateObj = new CaseManagementDO();
        updateObj.setId(caseId);
        // updateObj.setCaseCardUrl(url); // TODO 设置生成的个案卡URL
        caseManagementMapper.updateById(updateObj);
        return null;
    }

    @Override
    public void closeCase(Long caseId, Integer closeType, String closeReason) {
        // TODO 业务方法：结案归档
        //  1. 校验专案存在且状态为进行中（status=0），否则抛出 CASE_MANAGEMENT_STATUS_ERROR
        //  2. 校验 closeType 合法性
        //  3. 如 closeType=1 康复达标，可调用 CaseRecoveryStandardService.checkRecovery 验证
        //  4. 更新专案状态为已结案（status=1），回写结案日期、结案类型、结案原因
        validateCaseManagementExists(caseId);
        CaseManagementDO updateObj = new CaseManagementDO();
        updateObj.setId(caseId);
        updateObj.setStatus(1); // 1已结案
        updateObj.setCloseDate(LocalDate.now());
        updateObj.setCloseType(closeType);
        updateObj.setCloseReason(closeReason);
        caseManagementMapper.updateById(updateObj);
    }

    @Override
    public List<CaseManagementDO> selectByChildAndType(Long childId, Integer caseType) {
        // 按儿童ID和专案类型查询（基于 selectListByChildId 后内存过滤，或后续扩展 Mapper 方法）
        List<CaseManagementDO> list = caseManagementMapper.selectListByChildId(childId);
        return list.stream().filter(item -> caseType == null || caseType.equals(item.getCaseType())).toList();
    }

}
