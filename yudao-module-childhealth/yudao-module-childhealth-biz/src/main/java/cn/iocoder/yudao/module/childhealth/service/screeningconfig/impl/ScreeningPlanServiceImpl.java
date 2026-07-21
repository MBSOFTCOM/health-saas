package cn.iocoder.yudao.module.childhealth.service.screeningconfig.impl;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screeningconfig.ScreeningPlanDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.screeningconfig.ScreeningPlanMapper;
import cn.iocoder.yudao.module.childhealth.service.screeningconfig.ScreeningPlanService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.HEALTH_SCREENING_PLAN_CODE_DUPLICATE;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.HEALTH_SCREENING_PLAN_NOT_EXISTS;

/**
 * 体检方案配置 Service 实现类
 *
 * 模块: C.五健筛查配置
 * 创建日期: 2026-07-20
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class ScreeningPlanServiceImpl implements ScreeningPlanService {

    @Resource
    private ScreeningPlanMapper screeningPlanMapper;

    @Override
    public Long createScreeningPlan(Object saveReqVO) {
        // TODO 后续替换为 ScreeningPlanSaveReqVO
        ScreeningPlanDO plan = BeanUtils.toBean(saveReqVO, ScreeningPlanDO.class);
        // 编码唯一性校验
        if (plan.getPlanCode() != null
                && screeningPlanMapper.selectByPlanCode(plan.getPlanCode()) != null) {
            throw exception(HEALTH_SCREENING_PLAN_CODE_DUPLICATE);
        }
        screeningPlanMapper.insert(plan);
        return plan.getId();
    }

    @Override
    public void updateScreeningPlan(Object saveReqVO) {
        // TODO 后续替换为 ScreeningPlanSaveReqVO
        ScreeningPlanDO updateObj = BeanUtils.toBean(saveReqVO, ScreeningPlanDO.class);
        validateScreeningPlanExists(updateObj.getId());
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
    public PageResult<ScreeningPlanDO> getScreeningPlanPage(PageParam pageParam) {
        // TODO 后续替换为 ScreeningPlanPageReqVO，并增加查询条件
        return screeningPlanMapper.selectPage(pageParam, null);
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
    public List<ScreeningPlanDO> selectActiveList() {
        return screeningPlanMapper.selectActiveList();
    }

    private void validateScreeningPlanExists(Long id) {
        if (id == null || screeningPlanMapper.selectById(id) == null) {
            throw exception(HEALTH_SCREENING_PLAN_NOT_EXISTS);
        }
    }

}
