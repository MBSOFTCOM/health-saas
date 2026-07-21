package cn.iocoder.yudao.module.childhealth.service.casemanagement;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.casemanagement.HighRiskFollowupDO;

import java.util.List;

/**
 * 高危儿随访 Service 接口
 *
 * 模块: B.高危儿专案管理
 * 创建日期: 2026-07-20
 */
public interface HighRiskFollowupService {

    /**
     * 创建高危儿随访
     *
     * @param createReqVO 创建信息（占位，后续替换为 HighRiskFollowupSaveReqVO）
     * @return 编号
     */
    Long createHighRiskFollowup(Object createReqVO);

    /**
     * 更新高危儿随访
     *
     * @param updateReqVO 更新信息（占位，后续替换为 HighRiskFollowupSaveReqVO）
     */
    void updateHighRiskFollowup(Object updateReqVO);

    /**
     * 删除高危儿随访
     *
     * @param id 编号
     */
    void deleteHighRiskFollowup(Long id);

    /**
     * 获得高危儿随访
     *
     * @param id 编号
     * @return 高危儿随访
     */
    HighRiskFollowupDO getHighRiskFollowup(Long id);

    /**
     * 获得高危儿随访分页
     *
     * @param pageParam 分页查询（占位，后续替换为 HighRiskFollowupPageReqVO）
     * @return 高危儿随访分页
     */
    PageResult<HighRiskFollowupDO> getHighRiskFollowupPage(PageParam pageParam);

    /**
     * 按高危新生儿ID查询随访列表
     *
     * @param newbornId 高危新生儿ID
     * @return 随访列表
     */
    List<HighRiskFollowupDO> selectListByNewbornId(Long newbornId);

}
