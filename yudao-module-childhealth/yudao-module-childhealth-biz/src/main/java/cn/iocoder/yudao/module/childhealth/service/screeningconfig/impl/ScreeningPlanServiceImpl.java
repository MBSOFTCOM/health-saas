package cn.iocoder.yudao.module.childhealth.service.screeningconfig.impl;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.screeningconfig.vo.ScreeningPlanPageReqVO;
import cn.iocoder.yudao.module.childhealth.controller.admin.screeningconfig.vo.ScreeningPlanSaveReqVO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screeningconfig.ScreeningPlanDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.screeningconfig.ScreeningPlanMapper;
import cn.iocoder.yudao.module.childhealth.service.screeningconfig.ScreeningPlanService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.HEALTH_SCREENING_PLAN_CODE_DUPLICATE;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.HEALTH_SCREENING_PLAN_NOT_EXISTS;

/**
 * 体检方案配置 Service 实现类
 *
 * 模块: C.五健筛查配置
 */
@Service
@Validated
@Slf4j
public class ScreeningPlanServiceImpl implements ScreeningPlanService {

    @Resource
    private ScreeningPlanMapper screeningPlanMapper;

    @Override
    public Long createScreeningPlan(ScreeningPlanSaveReqVO saveReqVO) {
        // 编码唯一性校验
        validatePlanCodeUnique(null, saveReqVO.getPlanCode());
        ScreeningPlanDO plan = BeanUtils.toBean(saveReqVO, ScreeningPlanDO.class);
        // 若设为默认，先清理同类型下的旧默认
        if (Integer.valueOf(1).equals(plan.getDefaultPlan())) {
            clearDefaultByPlanType(plan.getPlanType());
        }
        screeningPlanMapper.insert(plan);
        return plan.getId();
    }

    @Override
    public void updateScreeningPlan(ScreeningPlanSaveReqVO saveReqVO) {
        validateScreeningPlanExists(saveReqVO.getId());
        validatePlanCodeUnique(saveReqVO.getId(), saveReqVO.getPlanCode());
        ScreeningPlanDO updateObj = BeanUtils.toBean(saveReqVO, ScreeningPlanDO.class);
        // 若设为默认，先清理同类型下其它默认
        if (Integer.valueOf(1).equals(updateObj.getDefaultPlan())) {
            clearDefaultByPlanType(updateObj.getPlanType());
        }
        screeningPlanMapper.updateById(updateObj);
    }

    @Override
    public void deleteScreeningPlan(Long id) {
        validateScreeningPlanExists(id);
        screeningPlanMapper.deleteById(id);
    }

    @Override
    public ScreeningPlanDO getScreeningPlan(Long id) {
        return screeningPlanMapper.selectById(id);
    }

    @Override
    public PageResult<ScreeningPlanDO> getScreeningPlanPage(ScreeningPlanPageReqVO pageReqVO) {
        return screeningPlanMapper.selectPage(pageReqVO);
    }

    @Override
    public ScreeningPlanDO selectByCode(String planCode) {
        return screeningPlanMapper.selectByPlanCode(planCode);
    }

    @Override
    public ScreeningPlanDO selectDefaultPlan(Integer planType) {
        return screeningPlanMapper.selectDefaultByPlanType(planType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setDefaultPlan(Long id, Integer planType) {
        validateScreeningPlanExists(id);
        // 1. 先把同类型下所有默认方案置为非默认
        clearDefaultByPlanType(planType);
        // 2. 再把当前方案置为默认
        ScreeningPlanDO updateObj = new ScreeningPlanDO();
        updateObj.setId(id);
        updateObj.setDefaultPlan(1);
        screeningPlanMapper.updateById(updateObj);
    }

    @Override
    public List<ScreeningPlanDO> selectActiveList() {
        return screeningPlanMapper.selectActiveList();
    }

    private void validateScreeningPlanExists(Long id) {
        if (id == null || screeningPlanMapper.selectById(id) == null) {
            throw exception(HEALTH_SCREENING_PLAN_NOT_EXISTS);
        }
    }

    private void validatePlanCodeUnique(Long id, String planCode) {
        if (planCode == null) {
            return;
        }
        ScreeningPlanDO existing = screeningPlanMapper.selectByPlanCode(planCode);
        if (existing == null) {
            return;
        }
        if (id == null || !existing.getId().equals(id)) {
            throw exception(HEALTH_SCREENING_PLAN_CODE_DUPLICATE);
        }
    }

    /**
     * 清理同类型下所有默认方案（置 default_plan=0）
     */
    private void clearDefaultByPlanType(Integer planType) {
        if (planType == null) {
            return;
        }
        List<ScreeningPlanDO> defaults = screeningPlanMapper.selectDefaultListByPlanType(planType);
        for (ScreeningPlanDO old : defaults) {
            ScreeningPlanDO clear = new ScreeningPlanDO();
            clear.setId(old.getId());
            clear.setDefaultPlan(0);
            screeningPlanMapper.updateById(clear);
        }
    }

}
