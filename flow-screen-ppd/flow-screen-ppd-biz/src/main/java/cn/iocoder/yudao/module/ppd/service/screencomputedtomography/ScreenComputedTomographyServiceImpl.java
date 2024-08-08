package cn.iocoder.yudao.module.ppd.service.screencomputedtomography;


import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.ppd.controller.admin.screencomputedtomography.vo.*;
import cn.iocoder.yudao.module.ppd.controller.admin.screensum.vo.CommonReq;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screencomputedtomography.ScreenComputedTomographyDO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screensum.ScreenSumDO;
import cn.iocoder.yudao.module.ppd.dal.mysql.screencomputedtomography.ScreenComputedTomographyMapper;
import cn.iocoder.yudao.module.ppd.dal.mysql.screensum.ScreenSumMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.validation.annotation.Validated;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.cd.enums.ErrorCodeConstants.SCREEN_CHEST_RADIOGRAPH_NOT_EXISTS;

/**
 * ct、dr组 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class ScreenComputedTomographyServiceImpl implements ScreenComputedTomographyService {

    @Resource
    private ScreenComputedTomographyMapper screenComputedTomographyMapper;

    @Resource
    private ScreenSumMapper screenSumMapper;

    @Override
    public Long createScreenComputedTomography(ScreenComputedTomographySaveReqVO createReqVO) {
        // 插入
        ScreenComputedTomographyDO screenComputedTomography = BeanUtils.toBean(createReqVO, ScreenComputedTomographyDO.class);
        screenComputedTomographyMapper.insert(screenComputedTomography);
        // 返回
        return screenComputedTomography.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createScreenComputedTomographyTrans(ScreenComputedTomographySaveReqVO createReqVO) {
        CommonReq commonReq = new CommonReq(createReqVO.getIdNum(), createReqVO.getYear() + "", createReqVO.getScreenType());
        Integer countExists = screenSumMapper.countExists(commonReq);
//        患者在某年的某筛查类型中是否有筛查流程记录
        if (countExists.equals(0)){
            throw exception(new ErrorCode(400000,"该患者"+createReqVO.getYear()+"年还未进行相关类型的筛查"));
        }
        try {
            ScreenSumDO maxOrderSumDO = screenSumMapper.getMaxOrderSumDO(commonReq);
//            是否含有最大次序的记录
            if (maxOrderSumDO==null){
                throw new NullPointerException();
            }
            if (maxOrderSumDO.getScreenOrder()==null || maxOrderSumDO.getScreenOrder().equals(0)){
                throw exception(new ErrorCode(400000,"筛查的次序不正确"));
            }
//            流程表是否完整
            if (maxOrderSumDO.getCollectId()==null || maxOrderSumDO.getPpdId()==null ){
                throw exception(new ErrorCode(400000,"该患者"+createReqVO.getYear()+"年还未进行相关类型的采集组或ppd组筛查"));
            }
            createReqVO.setScreenOrder(maxOrderSumDO.getScreenOrder());
            Long computedTomographyId = createScreenComputedTomography(createReqVO);
            maxOrderSumDO.setComputedTomographyId(computedTomographyId);
            maxOrderSumDO.setCurFinish("胸片组");
            screenSumMapper.updateById(maxOrderSumDO);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw exception(new ErrorCode(40000,"保存CT失败:\n"+e.getMessage()));
        }
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer getCreateOrder(ScreenComputedTomographySaveReqVO createReqVO) {
        CommonReq commonReq = new CommonReq(createReqVO.getIdNum(), createReqVO.getYear() + "", createReqVO.getScreenType());
        Integer countExists = screenSumMapper.countExists(commonReq);
//        患者在某年的某筛查类型中是否有筛查流程记录
        if (countExists.equals(0)){
            throw exception(new ErrorCode(400000,"该患者"+createReqVO.getYear()+"年还未进行相关类型的筛查"));
        }
        try {
            ScreenSumDO maxOrderSumDO = screenSumMapper.getMaxOrderSumDO(commonReq);
//            是否含有最大次序的记录
            if (maxOrderSumDO==null){
                throw new NullPointerException();
            }
            if (maxOrderSumDO.getScreenOrder()==null || maxOrderSumDO.getScreenOrder().equals(0)){
                throw exception(new ErrorCode(400000,"筛查的次序不正确"));
            }
//            流程表是否完整
            if (maxOrderSumDO.getCollectId()==null || maxOrderSumDO.getPpdId()==null ){
                throw exception(new ErrorCode(400000,"该患者"+createReqVO.getYear()+"年还未进行相关类型的采集组或ppd组筛查"));
            }
            return maxOrderSumDO.getScreenOrder();
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw exception(new ErrorCode(40000,"保存CT失败:\n"+e.getMessage()));
        }
    }

    @Override
    public void updateScreenComputedTomography(ScreenComputedTomographySaveReqVO updateReqVO) {
        // 校验存在
        validateScreenComputedTomographyExists(updateReqVO.getId());
        // 更新
        ScreenComputedTomographyDO updateObj = BeanUtils.toBean(updateReqVO, ScreenComputedTomographyDO.class);
        screenComputedTomographyMapper.updateById(updateObj);
    }

    @Override
    public void deleteScreenComputedTomography(Long id) {
        // 校验存在
        validateScreenComputedTomographyExists(id);
        // 删除
        screenComputedTomographyMapper.deleteById(id);
    }

    private void validateScreenComputedTomographyExists(Long id) {
        if (screenComputedTomographyMapper.selectById(id) == null) {
            throw exception(SCREEN_CHEST_RADIOGRAPH_NOT_EXISTS);
        }
    }

    @Override
    public ScreenComputedTomographyDO getScreenComputedTomography(Long id) {
        return screenComputedTomographyMapper.selectById(id);
    }

    @Override
    public ComputedTomographyStatistics getTimeStatistics(Integer screenType) {
        LocalDate currentDate = LocalDate.now();
        Integer year=currentDate.getYear();
        Integer month = currentDate.getMonthValue();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String curDay = currentDate.format(formatter);

        ComputedTomographyStatistics statistics = screenComputedTomographyMapper.getTypeStatistics(screenType,year);
        statistics.setYearNum(screenComputedTomographyMapper.getYearTimeStatistics(year+"",screenType));
        statistics.setMonthNum(screenComputedTomographyMapper.getMonthTimeStatistics(year+"",month+"",screenType));
        statistics.setDayNum(screenComputedTomographyMapper.getDayTimeStatistics(curDay,screenType));
        return statistics;
    }

    @Override
    public PageResult<ScreenComputedTomographyPersonRespVO> getScreenComputedTomographyPage(ScreenComputedTomographyPersonPageReqVO pageReqVO) {
        Integer moreTypeNum=0;
        List<Integer> moreType = pageReqVO.getMoreType();
        if (moreType!=null && !moreType.isEmpty()){
            for (Integer integer : moreType) {
                moreTypeNum+=integer;
            }
            pageReqVO.setMoreTypeNum(moreTypeNum);
        }
        if (pageReqVO.getPageNo() < 1 || pageReqVO.getPageSize() < 1){
//            throw exception(5000,"分页参数错误");
        }

        String startTime = pageReqVO.getStartTime();
        String endTime = pageReqVO.getEndTime();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (startTime!=null && endTime!=null){
            try{
                Date startDate = sdf.parse(startTime);
                Date endDate = sdf.parse(endTime);

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(startDate);
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                Date previousDay = calendar.getTime();
                String previousDayStr = sdf.format(previousDay);
                pageReqVO.setStartTime(previousDayStr);

                // 往后一天
                calendar.setTime(endDate);
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                Date nextDay = calendar.getTime();
                String nextDayStr = sdf.format(nextDay);
                pageReqVO.setEndTime(nextDayStr);
            }catch (Exception e){
                log.error("日期转换错误",e);
            }
    }
        pageReqVO.setPageNo((pageReqVO.getPageNo()-1)*pageReqVO.getPageSize());
        return new PageResult<>(screenComputedTomographyMapper.selectScreenComputedTomographyPage(pageReqVO),screenComputedTomographyMapper.countScreenComputedTomographyPage(pageReqVO));
    }

    @Override
    public List<ComputedTomographyOrderRespVO> getOrderAndTime(Long personId, Integer screenType, Integer year) {
        return screenComputedTomographyMapper.getOrderAndTime(personId,screenType,year);
    }

    @Override
    public Integer getMaxOrder(String idNum,Integer screenType,Integer year) {
        return screenComputedTomographyMapper.getMaxOrder(idNum,screenType,year);
    }

    @Override
    public ScreenComputedTomographyRespVO getOneByPersonIdNum(String idNum,Integer screenOrder,Integer screenType,Integer year) {
        return screenComputedTomographyMapper.getOneByPersonIdNum(idNum,screenOrder,screenType,year);
    }

    @Override
    public Integer getOutcomeByOrder(ScreenComputedTomographyPageReqVO reqVO) {
        return screenComputedTomographyMapper.getOutcomeByOrder(reqVO);
    }
}