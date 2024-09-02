package cn.iocoder.yudao.module.ppd.service.screensum;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.ppd.controller.admin.screensum.vo.CommonReq;
import cn.iocoder.yudao.module.ppd.controller.admin.screensum.vo.ScreenSumPageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screensum.vo.ScreenSumSaveReqVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screensum.ScreenSumDO;
import jakarta.validation.Valid;


/**
 * 汇总 Service 接口
 *
 * @author 芋道源码
 */
public interface ScreenSumService {

    String COLLECT="采集组";
    String PPD="ppd组";
    String CT="胸片组";
    String DR="胸片组";


    /**
     * 创建汇总
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createScreenSum(@Valid ScreenSumSaveReqVO createReqVO);

    /**
     * 更新汇总
     *
     * @param updateReqVO 更新信息
     */
    void updateScreenSum(@Valid ScreenSumSaveReqVO updateReqVO);

    /**
     * 获得汇总
     *
     * @param id 编号
     * @return 汇总
     */
    ScreenSumDO getScreenSum(Long id);

    /**
     * 获得汇总分页
     *
     * @param pageReqVO 分页查询
     * @return 汇总分页
     */
    PageResult<ScreenSumDO> getScreenSumPage(ScreenSumPageReqVO pageReqVO);

    Integer countByPersonId(CommonReq req);

    /**
     * 获取最近的体检单
     * @param idNum 身份证
     * @param screenType 筛查类型,默认为2
     * @return ScreenSumDO
     */
    ScreenSumDO getSumByIdNumLast(String idNum,Integer screenType);
}