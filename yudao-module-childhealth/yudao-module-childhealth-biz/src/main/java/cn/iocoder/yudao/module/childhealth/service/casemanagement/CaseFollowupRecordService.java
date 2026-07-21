package cn.iocoder.yudao.module.childhealth.service.casemanagement;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.casemanagement.CaseFollowupRecordDO;

import java.util.List;

/**
 * 专案随访记录 Service 接口
 *
 * 模块: B.专案管理
 * 创建日期: 2026-07-20
 */
public interface CaseFollowupRecordService {

    /**
     * 创建专案随访记录
     *
     * @param createReqVO 创建信息（占位，后续替换为 CaseFollowupRecordSaveReqVO）
     * @return 编号
     */
    Long createCaseFollowupRecord(Object createReqVO);

    /**
     * 更新专案随访记录
     *
     * @param updateReqVO 更新信息（占位，后续替换为 CaseFollowupRecordSaveReqVO）
     */
    void updateCaseFollowupRecord(Object updateReqVO);

    /**
     * 删除专案随访记录
     *
     * @param id 编号
     */
    void deleteCaseFollowupRecord(Long id);

    /**
     * 获得专案随访记录
     *
     * @param id 编号
     * @return 专案随访记录
     */
    CaseFollowupRecordDO getCaseFollowupRecord(Long id);

    /**
     * 获得专案随访记录分页
     *
     * @param pageParam 分页查询（占位，后续替换为 CaseFollowupRecordPageReqVO）
     * @return 专案随访记录分页
     */
    PageResult<CaseFollowupRecordDO> getCaseFollowupRecordPage(PageParam pageParam);

    /**
     * 按专案ID查询随访记录列表
     *
     * @param caseId 专案ID
     * @return 随访记录列表
     */
    List<CaseFollowupRecordDO> selectListByCaseId(Long caseId);

    /**
     * 批量新增专案随访记录
     *
     * @param records 随访记录列表
     * @return 新增数量
     */
    int batchInsert(List<CaseFollowupRecordDO> records);

}
