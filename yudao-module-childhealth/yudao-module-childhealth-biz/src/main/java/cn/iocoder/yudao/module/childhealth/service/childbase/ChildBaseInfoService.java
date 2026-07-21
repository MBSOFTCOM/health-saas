package cn.iocoder.yudao.module.childhealth.service.childbase;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.controller.admin.childbase.vo.*;

/**
 * 儿童基本信息 Service 接口
 *
 * @author 系统
 */
public interface ChildBaseInfoService {

    /**
     * 创建儿童档案（含监护人信息）
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createChildBaseInfo(ChildBaseInfoCreateReqVO createReqVO);

    /**
     * 更新儿童档案
     *
     * @param updateReqVO 更新信息
     */
    void updateChildBaseInfo(ChildBaseInfoUpdateReqVO updateReqVO);

    /**
     * 删除儿童档案
     *
     * @param id 编号
     */
    void deleteChildBaseInfo(Long id);

    /**
     * 获得儿童档案详情
     *
     * @param id 编号
     * @return 儿童档案
     */
    ChildBaseInfoRespVO getChildBaseInfo(Long id);

    /**
     * 获得儿童档案分页列表
     *
     * @param pageReqVO 分页条件
     * @return 儿童档案分页列表
     */
    PageResult<ChildBaseInfoRespVO> getChildBaseInfoPage(ChildBaseInfoPageReqVO pageReqVO);

    /**
     * 根据儿童ID生成二维码
     *
     * @param id 儿童ID
     * @return 二维码URL
     */
    String generateQrCode(Long id);

    /**
     * 根据儿童编码获取二维码URL
     *
     * @param childCode 儿童编码
     * @return 二维码URL
     */
    String getQrCodeByChildCode(String childCode);

    // ==================== 高危识别与评估 ====================

    /**
     * 根据分娩信息自动识别高危因素，自动划分高危管理等级
     * 需求3：根据儿童分娩信息支持自动识别高危儿早产儿等高危因素，自动划分高危儿管理等级
     *
     * @param childId 儿童ID
     * @return 识别出的高危因素列表
     */
    java.util.List<String> autoDetectHighRisk(Long childId);

    /**
     * 手工评估儿童高危情况
     * 需求4：支持手工评估儿童高危情况
     *
     * @param childId 儿童ID
     * @param factorCodes 勾选的高危因素编码列表
     * @param assessmentDoctor 评估医生ID
     * @return 高危等级
     */
    Integer manualAssessHighRisk(Long childId, java.util.List<String> factorCodes, Long assessmentDoctor);

    /**
     * 获取所有启用的高危因素配置
     *
     * @return 高危因素配置列表
     */
    java.util.List<cn.iocoder.yudao.module.childhealth.dal.dataobject.childbase.HighRiskFactorConfigDO> getHighRiskFactorConfigs();

    /**
     * 获取儿童的新生儿住院诊断列表（含预警）
     * 需求5：支持自动抓取新生儿住院诊断数据，在建档时预警提醒
     *
     * @param childId 儿童ID
     * @return 诊断列表
     */
    java.util.List<cn.iocoder.yudao.module.childhealth.dal.dataobject.childbase.NeonatalDiagnosisDO> getNeonatalDiagnosisList(Long childId);

    /**
     * 根据儿童ID获取分娩信息
     *
     * @param childId 儿童ID
     * @return 分娩信息
     */
    cn.iocoder.yudao.module.childhealth.dal.dataobject.childbase.DeliveryInfoDO getDeliveryInfo(Long childId);

    /**
     * 根据儿童ID获取家庭信息
     *
     * @param childId 儿童ID
     * @return 家庭信息
     */
    cn.iocoder.yudao.module.childhealth.dal.dataobject.childbase.FamilyInfoDO getFamilyInfo(Long childId);

    /**
     * 根据儿童ID获取免疫信息列表
     *
     * @param childId 儿童ID
     * @return 免疫信息列表
     */
    java.util.List<cn.iocoder.yudao.module.childhealth.dal.dataobject.childbase.ImmunizationInfoDO> getImmunizationList(Long childId);

}