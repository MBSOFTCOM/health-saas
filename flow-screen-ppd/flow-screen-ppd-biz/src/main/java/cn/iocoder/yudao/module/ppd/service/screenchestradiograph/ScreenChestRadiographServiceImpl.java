package cn.iocoder.yudao.module.ppd.service.screenchestradiograph;




import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.ppd.controller.admin.screenchestradiograph.vo.*;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenchestradiograph.ScreenChestRadiographDO;
import cn.iocoder.yudao.module.ppd.dal.mysql.screenchestradiograph.ScreenChestRadiographMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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
public class ScreenChestRadiographServiceImpl implements ScreenChestRadiographService {

    @Resource
    private ScreenChestRadiographMapper screenChestRadiographMapper;

    @Override
    public Long createScreenChestRadiograph(ScreenChestRadiographSaveReqVO createReqVO) {
        // 插入
        ScreenChestRadiographDO screenChestRadiograph = BeanUtils.toBean(createReqVO, ScreenChestRadiographDO.class);
        screenChestRadiographMapper.insert(screenChestRadiograph);
        // 返回
        return screenChestRadiograph.getId();
    }

    @Override
    public void updateScreenChestRadiograph(ScreenChestRadiographSaveReqVO updateReqVO) {
        // 校验存在
        validateScreenChestRadiographExists(updateReqVO.getId());
        // 更新
        ScreenChestRadiographDO updateObj = BeanUtils.toBean(updateReqVO, ScreenChestRadiographDO.class);
        screenChestRadiographMapper.updateById(updateObj);
    }

    @Override
    public void deleteScreenChestRadiograph(Long id) {
        // 校验存在
        validateScreenChestRadiographExists(id);
        // 删除
        screenChestRadiographMapper.deleteById(id);
    }

    private void validateScreenChestRadiographExists(Long id) {
        if (screenChestRadiographMapper.selectById(id) == null) {
            throw exception(SCREEN_CHEST_RADIOGRAPH_NOT_EXISTS);
        }
    }

    @Override
    public ScreenChestRadiographDO getScreenChestRadiograph(Long id) {
        return screenChestRadiographMapper.selectById(id);
    }

    @Override
    public ChestRadiographStatistics getTimeStatistics(Integer screenType) {
        LocalDate currentDate = LocalDate.now();
        Integer year = currentDate.getYear();
        Integer month = currentDate.getMonthValue();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String curDay = currentDate.format(formatter);

        ChestRadiographStatistics statistics = screenChestRadiographMapper.getTypeStatistics(screenType);
        statistics.setYearNum(screenChestRadiographMapper.getYearTimeStatistics(year+"",screenType));
        statistics.setMonthNum(screenChestRadiographMapper.getMonthTimeStatistics(year+"",month+"",screenType));
        statistics.setDayNum(screenChestRadiographMapper.getDayTimeStatistics(curDay,screenType));
        return statistics;
    }

    @Override
    public PageResult<ScreenChestRadiographPersonRespVO> getScreenChestRadiographPage(ScreenChestRadiographPersonPageReqVO pageReqVO) {
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
        return new PageResult<>(screenChestRadiographMapper.selectScreenChestRadiographPage(pageReqVO),screenChestRadiographMapper.countScreenChestRadiographPage(pageReqVO));
    }

    @Override
    public List<ChestRadiographOrderRespVO> getOrderAndTime(Long personId) {
        return screenChestRadiographMapper.getOrderAndTime(personId);
    }

    @Override
    public Integer getMaxOrder(Long personId,Integer screenType) {
        return screenChestRadiographMapper.getMaxOrder(personId,screenType);
    }

    @Override
    public ScreenChestRadiographRespVO getOneByPersonId(Long personId,Integer screenOrder,Integer screenType) {
        return screenChestRadiographMapper.getOneByPersonId(personId,screenOrder,screenType);
    }
}