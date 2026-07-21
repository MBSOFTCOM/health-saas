package cn.iocoder.yudao.module.childhealth.service.screening;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.record.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.ScreeningRecordDO;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 筛查记录 Service 接口
 *
 * @author 芋道源码
 */
public interface ScreeningRecordService {

    /**
     * 创建筛查记录
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createScreeningRecord(@Valid ScreeningRecordSaveReqVO createReqVO);

    /**
     * 更新筛查记录
     *
     * @param updateReqVO 更新信息
     */
    void updateScreeningRecord(@Valid ScreeningRecordSaveReqVO updateReqVO);

    /**
     * 删除筛查记录
     *
     * @param id 编号
     */
    void deleteScreeningRecord(Long id);

    /**
     * 获得筛查记录
     *
     * @param id 编号
     * @return 筛查记录
     */
    ScreeningRecordDO getScreeningRecord(Long id);

    /**
     * 获得筛查记录分页
     *
     * @param pageReqVO 分页查询
     * @return 筛查记录分页
     */
    PageResult<ScreeningRecordDO> getScreeningRecordPage(ScreeningRecordPageReqVO pageReqVO);

    /**
     * 获得筛查记录列表
     *
     * @param listReqVO 列表查询
     * @return 筛查记录列表
     */
    List<ScreeningRecordDO> getScreeningRecordList(ScreeningRecordListReqVO listReqVO);

    /**
     * 审核筛查记录
     *
     * @param id 编号
     * @param auditDoctor 审核医生ID
     */
    void auditScreeningRecord(Long id, Long auditDoctor);

    /**
     * 提交审核
     *
     * @param id 编号
     */
    void submitAudit(Long id);

}