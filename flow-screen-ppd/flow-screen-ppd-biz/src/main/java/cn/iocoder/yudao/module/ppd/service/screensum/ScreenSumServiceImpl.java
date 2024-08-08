package cn.iocoder.yudao.module.ppd.service.screensum;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.ppd.controller.admin.screensum.vo.CommonReq;
import cn.iocoder.yudao.module.ppd.controller.admin.screensum.vo.ScreenSumPageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screensum.vo.ScreenSumSaveReqVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screensum.ScreenSumDO;
import cn.iocoder.yudao.module.ppd.dal.mysql.screensum.ScreenSumMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;



/**
 * 汇总 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ScreenSumServiceImpl implements ScreenSumService {

    @Resource
    private ScreenSumMapper screenSumMapper;

    @Override
    public Long createScreenSum(ScreenSumSaveReqVO createReqVO) {
        // 插入
        ScreenSumDO screenSum = BeanUtils.toBean(createReqVO, ScreenSumDO.class);
        screenSumMapper.insert(screenSum);
        // 返回
        return screenSum.getId();
    }

    @Override
    public void updateScreenSum(ScreenSumSaveReqVO updateReqVO) {
        // 校验存在
        if (validateScreenSumExists(new CommonReq(updateReqVO.getPersonId(),updateReqVO.getYear(),null,updateReqVO.getScreenType()))){
            createScreenSum(updateReqVO);
        }
        // 更新
        ScreenSumDO updateObj = BeanUtils.toBean(updateReqVO, ScreenSumDO.class);
//        screenSumMapper.updateById(updateObj);
        screenSumMapper.update(updateObj,new LambdaQueryWrapperX<ScreenSumDO>()
                .eq(ScreenSumDO::getPersonId,updateObj.getPersonId()));
    }

    /**
     * @param req CommonReq
     * @return Boolean true 新增汇总| false 修改
     */
    private Boolean validateScreenSumExists(CommonReq req) {
        if (screenSumMapper.countByPersonId(req) == 0) {
            return true;
        }
        return false;
    }

    @Override
    public ScreenSumDO getScreenSum(Long id) {
        return screenSumMapper.selectById(id);
    }

    @Override
    public PageResult<ScreenSumDO> getScreenSumPage(ScreenSumPageReqVO pageReqVO) {
        return screenSumMapper.selectPage(pageReqVO);
    }

    @Override
    public Integer countByPersonId(CommonReq req) {
        return screenSumMapper.countByPersonId(req);
    }
}