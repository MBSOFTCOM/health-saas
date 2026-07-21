package cn.iocoder.yudao.module.childhealth.service.screening;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.statistics.ScreeningStatisticsPageReqVO;
import cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.statistics.ScreeningStatisticsSaveReqVO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.ScreeningStatisticsDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.screening.ScreeningStatisticsMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.*;

/**
 * 筛查统计 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ScreeningStatisticsServiceImpl implements ScreeningStatisticsService {

    @Resource
    private ScreeningStatisticsMapper screeningStatisticsMapper;

    @Override
    public Long createScreeningStatistics(ScreeningStatisticsSaveReqVO createReqVO) {
        // 插入
        ScreeningStatisticsDO screeningStatistics = BeanUtils.toBean(createReqVO, ScreeningStatisticsDO.class);
        screeningStatisticsMapper.insert(screeningStatistics);
        return screeningStatistics.getId();
    }

    @Override
    public void updateScreeningStatistics(ScreeningStatisticsSaveReqVO updateReqVO) {
        // 校验存在
        validateScreeningStatisticsExists(updateReqVO.getId());
        // 更新
        ScreeningStatisticsDO updateObj = BeanUtils.toBean(updateReqVO, ScreeningStatisticsDO.class);
        screeningStatisticsMapper.updateById(updateObj);
    }

    @Override
    public void deleteScreeningStatistics(Long id) {
        // 校验存在
        validateScreeningStatisticsExists(id);
        // 删除
        screeningStatisticsMapper.deleteById(id);
    }

    private void validateScreeningStatisticsExists(Long id) {
        if (screeningStatisticsMapper.selectById(id) == null) {
            throw exception(SCREENING_STATISTICS_NOT_EXISTS);
        }
    }

    @Override
    public ScreeningStatisticsDO getScreeningStatistics(Long id) {
        return screeningStatisticsMapper.selectById(id);
    }

    @Override
    public PageResult<ScreeningStatisticsDO> getScreeningStatisticsPage(ScreeningStatisticsPageReqVO pageReqVO) {
        return screeningStatisticsMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ScreeningStatisticsDO> getScreeningStatisticsListByBatchId(Long batchId) {
        return screeningStatisticsMapper.selectListByBatchId(batchId);
    }

    @Override
    public List<ScreeningStatisticsDO> getScreeningStatisticsListBySchoolId(Long schoolId) {
        return screeningStatisticsMapper.selectListBySchoolId(schoolId);
    }

}