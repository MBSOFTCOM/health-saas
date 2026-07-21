package cn.iocoder.yudao.module.childhealth.service.screeningconfig.impl;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screeningconfig.ScreeningDepartmentDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.screeningconfig.ScreeningDepartmentMapper;
import cn.iocoder.yudao.module.childhealth.service.screeningconfig.ScreeningDepartmentService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.SCREENING_DEPARTMENT_CODE_DUPLICATE;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.SCREENING_DEPARTMENT_NOT_EXISTS;

/**
 * 筛查科室 Service 实现类
 *
 * 模块: C.五健筛查配置
 * 创建日期: 2026-07-20
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class ScreeningDepartmentServiceImpl implements ScreeningDepartmentService {

    @Resource
    private ScreeningDepartmentMapper screeningDepartmentMapper;

    @Override
    public Long createScreeningDepartment(Object saveReqVO) {
        // TODO 后续替换为 ScreeningDepartmentSaveReqVO
        ScreeningDepartmentDO department = BeanUtils.toBean(saveReqVO, ScreeningDepartmentDO.class);
        // 编码唯一性校验
        if (department.getDeptCode() != null
                && screeningDepartmentMapper.selectByDeptCode(department.getDeptCode()) != null) {
            throw exception(SCREENING_DEPARTMENT_CODE_DUPLICATE);
        }
        screeningDepartmentMapper.insert(department);
        return department.getId();
    }

    @Override
    public void updateScreeningDepartment(Object saveReqVO) {
        // TODO 后续替换为 ScreeningDepartmentSaveReqVO
        ScreeningDepartmentDO updateObj = BeanUtils.toBean(saveReqVO, ScreeningDepartmentDO.class);
        validateScreeningDepartmentExists(updateObj.getId());
        screeningDepartmentMapper.updateById(updateObj);
    }

    @Override
    public void deleteScreeningDepartment(Long id) {
        validateScreeningDepartmentExists(id);
        screeningDepartmentMapper.deleteById(id);
    }

    @Override
    public ScreeningDepartmentDO getScreeningDepartment(Long id) {
        return screeningDepartmentMapper.selectById(id);
    }

    @Override
    public PageResult<ScreeningDepartmentDO> getScreeningDepartmentPage(PageParam pageParam) {
        // TODO 后续替换为 ScreeningDepartmentPageReqVO，并增加查询条件
        return screeningDepartmentMapper.selectPage(pageParam, null);
    }

    @Override
    public ScreeningDepartmentDO selectByCode(String deptCode) {
        return screeningDepartmentMapper.selectByDeptCode(deptCode);
    }

    @Override
    public List<ScreeningDepartmentDO> selectActiveList() {
        return screeningDepartmentMapper.selectActiveList();
    }

    private void validateScreeningDepartmentExists(Long id) {
        if (id == null || screeningDepartmentMapper.selectById(id) == null) {
            throw exception(SCREENING_DEPARTMENT_NOT_EXISTS);
        }
    }

}
