package cn.iocoder.yudao.module.childhealth.service.casemanagement;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.casemanagement.CaseManagementDO;

import java.util.List;

/**
 * 专案主表 Service 接口
 *
 * 模块: B.专案管理
 * 创建日期: 2026-07-20
 */
public interface CaseManagementService {

    /**
     * 创建专案
     *
     * @param createReqVO 创建信息（占位，后续替换为 CaseManagementSaveReqVO）
     * @return 编号
     */
    Long createCaseManagement(Object createReqVO);

    /**
     * 更新专案
     *
     * @param updateReqVO 更新信息（占位，后续替换为 CaseManagementSaveReqVO）
     */
    void updateCaseManagement(Object updateReqVO);

    /**
     * 删除专案
     *
     * @param id 编号
     */
    void deleteCaseManagement(Long id);

    /**
     * 获得专案
     *
     * @param id 编号
     * @return 专案
     */
    CaseManagementDO getCaseManagement(Long id);

    /**
     * 获得专案分页
     *
     * @param pageParam 分页查询（占位，后续替换为 CaseManagementPageReqVO）
     * @return 专案分页
     */
    PageResult<CaseManagementDO> getCaseManagementPage(PageParam pageParam);

    /**
     * 业务方法：异常自动建专案
     * 根据异常检测/筛查/体检异常记录，自动建立对应类型的专案
     *
     * @param abnormalRecord 异常记录（占位，后续替换为具体异常记录类型）
     * @return 专案编号
     */
    Long autoCreateCaseFromAbnormal(Object abnormalRecord);

    /**
     * 业务方法：生成个案卡
     * 根据专案ID生成个案卡文件并回写URL
     *
     * @param caseId 专案ID
     * @return 个案卡文件URL
     */
    String generateCaseCard(Long caseId);

    /**
     * 业务方法：结案归档
     *
     * @param caseId      专案ID
     * @param closeType   结案类型 1康复达标 2转介 3其他
     * @param closeReason 结案原因
     */
    void closeCase(Long caseId, Integer closeType, String closeReason);

    /**
     * 按儿童ID和专案类型查询专案
     *
     * @param childId  儿童ID
     * @param caseType 专案类型
     * @return 专案列表
     */
    List<CaseManagementDO> selectByChildAndType(Long childId, Integer caseType);

}
