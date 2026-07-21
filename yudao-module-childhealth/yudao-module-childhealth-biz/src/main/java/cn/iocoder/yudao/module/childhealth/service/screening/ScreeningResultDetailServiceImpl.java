package cn.iocoder.yudao.module.childhealth.service.screening;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.resultDetail.ScreeningResultDetailPageReqVO;
import cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.resultDetail.ScreeningResultDetailSaveReqVO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.ScreeningResultDetailDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.screening.ScreeningResultDetailMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.*;

/**
 * 筛查结果明细 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ScreeningResultDetailServiceImpl implements ScreeningResultDetailService {

    @Resource
    private ScreeningResultDetailMapper screeningResultDetailMapper;

    @Override
    public Long createScreeningResultDetail(ScreeningResultDetailSaveReqVO createReqVO) {
        // 校验项目编码唯一（同一记录下）
        validateItemCodeUnique(null, createReqVO.getRecordId(), createReqVO.getItemCode());
        // 插入
        ScreeningResultDetailDO screeningResultDetail = BeanUtils.toBean(createReqVO, ScreeningResultDetailDO.class);
        screeningResultDetailMapper.insert(screeningResultDetail);
        return screeningResultDetail.getId();
    }

    @Override
    public void updateScreeningResultDetail(ScreeningResultDetailSaveReqVO updateReqVO) {
        // 校验存在
        validateScreeningResultDetailExists(updateReqVO.getId());
        // 校验项目编码唯一（同一记录下）
        validateItemCodeUnique(updateReqVO.getId(), updateReqVO.getRecordId(), updateReqVO.getItemCode());
        // 更新
        ScreeningResultDetailDO updateObj = BeanUtils.toBean(updateReqVO, ScreeningResultDetailDO.class);
        screeningResultDetailMapper.updateById(updateObj);
    }

    @Override
    public void deleteScreeningResultDetail(Long id) {
        // 校验存在
        validateScreeningResultDetailExists(id);
        // 删除
        screeningResultDetailMapper.deleteById(id);
    }

    private void validateScreeningResultDetailExists(Long id) {
        if (screeningResultDetailMapper.selectById(id) == null) {
            throw exception(SCREENING_RESULT_DETAIL_NOT_EXISTS);
        }
    }

    private void validateItemCodeUnique(Long id, Long recordId, String itemCode) {
        ScreeningResultDetailDO detail = screeningResultDetailMapper.selectByRecordIdAndItemCode(recordId, itemCode);
        if (detail == null) {
            return;
        }
        if (id == null) {
            throw exception(SCREENING_RESULT_DETAIL_DUPLICATE);
        }
        if (!detail.getId().equals(id)) {
            throw exception(SCREENING_RESULT_DETAIL_DUPLICATE);
        }
    }

    @Override
    public ScreeningResultDetailDO getScreeningResultDetail(Long id) {
        return screeningResultDetailMapper.selectById(id);
    }

    @Override
    public PageResult<ScreeningResultDetailDO> getScreeningResultDetailPage(ScreeningResultDetailPageReqVO pageReqVO) {
        return screeningResultDetailMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ScreeningResultDetailDO> getScreeningResultDetailListByRecordId(Long recordId) {
        return screeningResultDetailMapper.selectByRecordId(recordId);
    }

}