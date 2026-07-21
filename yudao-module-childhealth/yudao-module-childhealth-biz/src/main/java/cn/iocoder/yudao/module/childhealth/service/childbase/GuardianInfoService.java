package cn.iocoder.yudao.module.childhealth.service.childbase;

import cn.iocoder.yudao.module.childhealth.controller.admin.childbase.vo.GuardianInfoCreateReqVO;
import cn.iocoder.yudao.module.childhealth.controller.admin.childbase.vo.GuardianInfoRespVO;

import java.util.List;

/**
 * 监护人信息 Service 接口
 *
 * @author 系统
 */
public interface GuardianInfoService {

    /**
     * 创建监护人信息
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createGuardianInfo(GuardianInfoCreateReqVO createReqVO);

    /**
     * 更新监护人信息
     *
     * @param id 编号
     * @param updateReqVO 更新信息
     */
    void updateGuardianInfo(Long id, GuardianInfoCreateReqVO updateReqVO);

    /**
     * 删除监护人信息
     *
     * @param id 编号
     */
    void deleteGuardianInfo(Long id);

    /**
     * 获得监护人信息详情
     *
     * @param id 编号
     * @return 监护人信息
     */
    GuardianInfoRespVO getGuardianInfo(Long id);

    /**
     * 根据儿童ID获取监护人信息列表
     *
     * @param childId 儿童ID
     * @return 监护人信息列表
     */
    List<GuardianInfoRespVO> getGuardianInfoListByChildId(Long childId);

    /**
     * 根据儿童ID获取主要监护人信息
     *
     * @param childId 儿童ID
     * @return 主要监护人信息
     */
    GuardianInfoRespVO getPrimaryGuardianByChildId(Long childId);

}