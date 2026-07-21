package cn.iocoder.yudao.module.childhealth.service.casemanagement;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.casemanagement.CaseRecoveryStandardDO;

import java.util.List;

/**
 * 专案康复达标标准 Service 接口
 *
 * 模块: B.专案管理
 * 创建日期: 2026-07-20
 */
public interface CaseRecoveryStandardService {

    /**
     * 创建专案康复达标标准
     *
     * @param createReqVO 创建信息（占位，后续替换为 CaseRecoveryStandardSaveReqVO）
     * @return 编号
     */
    Long createCaseRecoveryStandard(Object createReqVO);

    /**
     * 更新专案康复达标标准
     *
     * @param updateReqVO 更新信息（占位，后续替换为 CaseRecoveryStandardSaveReqVO）
     */
    void updateCaseRecoveryStandard(Object updateReqVO);

    /**
     * 删除专案康复达标标准
     *
     * @param id 编号
     */
    void deleteCaseRecoveryStandard(Long id);

    /**
     * 获得专案康复达标标准
     *
     * @param id 编号
     * @return 专案康复达标标准
     */
    CaseRecoveryStandardDO getCaseRecoveryStandard(Long id);

    /**
     * 获得专案康复达标标准分页
     *
     * @param pageParam 分页查询（占位，后续替换为 CaseRecoveryStandardPageReqVO）
     * @return 专案康复达标标准分页
     */
    PageResult<CaseRecoveryStandardDO> getCaseRecoveryStandardPage(PageParam pageParam);

    /**
     * 按专案类型查询达标标准列表
     *
     * @param caseType 专案类型
     * @return 达标标准列表
     */
    List<CaseRecoveryStandardDO> selectListByCaseType(Integer caseType);

    /**
     * 业务方法：康复达标判断
     * 根据专案ID及最新随访指标数据，判断是否全部达标
     *
     * @param caseId          专案ID
     * @param indicatorValues 指标值（占位，后续替换为具体指标值Map/VO）
     * @return 是否达标
     */
    boolean checkRecovery(Long caseId, Object indicatorValues);

}
