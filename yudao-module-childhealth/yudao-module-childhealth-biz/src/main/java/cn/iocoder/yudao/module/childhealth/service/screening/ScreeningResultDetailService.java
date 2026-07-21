package cn.iocoder.yudao.module.childhealth.service.screening;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.resultDetail.ScreeningResultDetailPageReqVO;
import cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.resultDetail.ScreeningResultDetailSaveReqVO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.ScreeningResultDetailDO;
import jakarta.validation.Valid;

import java.util.List;

/**
 * 筛查结果明细 Service 接口
 *
 * @author 芋道源码
 */
public interface ScreeningResultDetailService {

    /**
     * 创建筛查结果明细
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createScreeningResultDetail(@Valid ScreeningResultDetailSaveReqVO createReqVO);

    /**
     * 更新筛查结果明细
     *
     * @param updateReqVO 更新信息
     */
    void updateScreeningResultDetail(@Valid ScreeningResultDetailSaveReqVO updateReqVO);

    /**
     * 删除筛查结果明细
     *
     * @param id 编号
     */
    void deleteScreeningResultDetail(Long id);

    /**
     * 获得筛查结果明细
     *
     * @param id 编号
     * @return 筛查结果明细
     */
    ScreeningResultDetailDO getScreeningResultDetail(Long id);

    /**
     * 获得筛查结果明细分页
     *
     * @param pageReqVO 分页查询
     * @return 筛查结果明细分页
     */
    PageResult<ScreeningResultDetailDO> getScreeningResultDetailPage(ScreeningResultDetailPageReqVO pageReqVO);

    /**
     * 获得筛查结果明细列表（根据记录ID）
     *
     * @param recordId 记录ID
     * @return 筛查结果明细列表
     */
    List<ScreeningResultDetailDO> getScreeningResultDetailListByRecordId(Long recordId);

}