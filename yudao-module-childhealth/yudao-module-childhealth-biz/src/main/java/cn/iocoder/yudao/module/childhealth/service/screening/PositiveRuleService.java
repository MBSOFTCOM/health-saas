package cn.iocoder.yudao.module.childhealth.service.screening;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.rule.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.PositiveRuleDO;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 阳性判定规则 Service 接口
 *
 * @author 芋道源码
 */
public interface PositiveRuleService {

    /**
     * 创建阳性判定规则
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createPositiveRule(@Valid PositiveRuleSaveReqVO createReqVO);

    /**
     * 更新阳性判定规则
     *
     * @param updateReqVO 更新信息
     */
    void updatePositiveRule(@Valid PositiveRuleSaveReqVO updateReqVO);

    /**
     * 删除阳性判定规则
     *
     * @param id 编号
     */
    void deletePositiveRule(Long id);

    /**
     * 获得阳性判定规则
     *
     * @param id 编号
     * @return 阳性判定规则
     */
    PositiveRuleDO getPositiveRule(Long id);

    /**
     * 获得阳性判定规则分页
     *
     * @param pageReqVO 分页查询
     * @return 阳性判定规则分页
     */
    PageResult<PositiveRuleDO> getPositiveRulePage(PositiveRulePageReqVO pageReqVO);

    /**
     * 获得阳性判定规则列表
     *
     * @param listReqVO 列表查询
     * @return 阳性判定规则列表
     */
    List<PositiveRuleDO> getPositiveRuleList(PositiveRuleListReqVO listReqVO);

    /**
     * 获取所有启用的规则
     *
     * @return 规则列表
     */
    List<PositiveRuleDO> getActiveRuleList();

}