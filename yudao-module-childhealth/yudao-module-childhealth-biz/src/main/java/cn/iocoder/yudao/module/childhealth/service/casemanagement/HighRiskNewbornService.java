package cn.iocoder.yudao.module.childhealth.service.casemanagement;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.casemanagement.HighRiskNewbornDO;

import java.util.List;

/**
 * 高危新生儿台账 Service 接口
 *
 * 模块: B.高危儿专案管理
 * 创建日期: 2026-07-20
 */
public interface HighRiskNewbornService {

    /**
     * 创建高危新生儿台账
     *
     * @param createReqVO 创建信息（占位，后续替换为 HighRiskNewbornSaveReqVO）
     * @return 编号
     */
    Long createHighRiskNewborn(Object createReqVO);

    /**
     * 更新高危新生儿台账
     *
     * @param updateReqVO 更新信息（占位，后续替换为 HighRiskNewbornSaveReqVO）
     */
    void updateHighRiskNewborn(Object updateReqVO);

    /**
     * 删除高危新生儿台账
     *
     * @param id 编号
     */
    void deleteHighRiskNewborn(Long id);

    /**
     * 获得高危新生儿台账
     *
     * @param id 编号
     * @return 高危新生儿台账
     */
    HighRiskNewbornDO getHighRiskNewborn(Long id);

    /**
     * 获得高危新生儿台账分页
     *
     * @param pageParam 分页查询（占位，后续替换为 HighRiskNewbornPageReqVO）
     * @return 高危新生儿台账分页
     */
    PageResult<HighRiskNewbornDO> getHighRiskNewbornPage(PageParam pageParam);

    /**
     * 业务方法：未建册随访预警
     * 扫描已建册但未按计划随访的高危新生儿，生成预警列表
     *
     * @return 预警的高危新生儿列表
     */
    List<HighRiskNewbornDO> autoRemindFollowup();

}
