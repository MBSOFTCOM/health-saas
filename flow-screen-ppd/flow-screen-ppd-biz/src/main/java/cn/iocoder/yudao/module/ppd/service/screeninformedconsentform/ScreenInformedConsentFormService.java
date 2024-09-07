package cn.iocoder.yudao.module.ppd.service.screeninformedconsentform;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.ppd.controller.admin.screeninformedconsentform.vo.ScreenInformedConsentFormPageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screeninformedconsentform.vo.ScreenInformedConsentFormRespVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screeninformedconsentform.vo.ScreenInformedConsentFormSaveReqVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screeninformedconsentform.ScreenInformedConsentFormDO;
import jakarta.validation.Valid;

/**
 * 知情同意书 Service 接口
 *
 * @author 福乐云
 */
public interface ScreenInformedConsentFormService {

    /**
     * 创建知情同意书
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createScreenInformedConsentForm(@Valid ScreenInformedConsentFormSaveReqVO createReqVO);

    /**
     * 更新知情同意书
     *
     * @param updateReqVO 更新信息
     */
    void updateScreenInformedConsentForm(@Valid ScreenInformedConsentFormSaveReqVO updateReqVO);

    /**
     * 删除知情同意书
     *
     * @param id 编号
     */
    void deleteScreenInformedConsentForm(Long id);

    /**
     * 获得知情同意书
     *
     * @param id 编号
     * @return 知情同意书
     */
    ScreenInformedConsentFormDO getScreenInformedConsentForm(Long id);
    /**
     * 获得知情同意书
     *
     * @param studentId 编号
     * @return 知情同意书
     */
    ScreenInformedConsentFormRespVO getLastInformedConsentForm(Long studentId);

    /**
     * 获得知情同意书分页
     *
     * @param pageReqVO 分页查询
     * @return 知情同意书分页
     */
    PageResult<ScreenInformedConsentFormDO> getScreenInformedConsentFormPage(ScreenInformedConsentFormPageReqVO pageReqVO);

}