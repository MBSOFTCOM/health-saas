package cn.iocoder.yudao.module.childhealth.service.device;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.device.RecheckCheckinDO;

import java.util.List;

/**
 * 复筛报到登记 Service 接口
 *
 * 模块: D. 移动端功能补全
 * 创建日期: 2026-07-20
 *
 * @author 芋道源码
 */
public interface RecheckCheckinService {

    /**
     * 创建复筛报到登记
     *
     * @param saveReqVO 创建信息（后续替换为 RecheckCheckinSaveReqVO）
     * @return 编号
     */
    Long createRecheckCheckin(Object saveReqVO);

    /**
     * 更新复筛报到登记
     *
     * @param saveReqVO 更新信息（后续替换为 RecheckCheckinSaveReqVO）
     */
    void updateRecheckCheckin(Object saveReqVO);

    /**
     * 删除复筛报到登记
     *
     * @param id 编号
     */
    void deleteRecheckCheckin(Long id);

    /**
     * 获得复筛报到登记
     *
     * @param id 编号
     * @return 复筛报到登记
     */
    RecheckCheckinDO getRecheckCheckin(Long id);

    /**
     * 获得复筛报到登记分页
     *
     * @param pageParam 分页查询（后续替换为 RecheckCheckinPageReqVO）
     * @return 复筛报到登记分页
     */
    PageResult<RecheckCheckinDO> getRecheckCheckinPage(PageParam pageParam);

    /**
     * 按报到编号查询
     *
     * @param checkinNo 报到编号
     * @return 复筛报到登记
     */
    RecheckCheckinDO selectByCheckinNo(String checkinNo);

    /**
     * 扫码报到
     *
     * @param qrcodeContent 扫码内容
     * @return 复筛报到登记
     */
    RecheckCheckinDO checkinByQrcode(String qrcodeContent);

    /**
     * 按现场状态查询报到记录列表
     *
     * @param onSiteStatus 现场状态 0待接诊 1已接诊 2已检查 3已离场
     * @return 复筛报到登记列表
     */
    List<RecheckCheckinDO> selectListByOnSiteStatus(Integer onSiteStatus);

}
