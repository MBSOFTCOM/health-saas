package cn.iocoder.yudao.module.childhealth.service.medical.impl;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.medical.LisPacsReportDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.medical.LisPacsReportMapper;
import cn.iocoder.yudao.module.childhealth.service.medical.LisPacsReportService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.LIS_PACS_REPORT_NOT_EXISTS;

/**
 * LIS/PACS 检验检查报告对接表 Service 实现类
 *
 * 模块: A. 儿童基础健康检查（A4-LIS/PACS检验检查报告对接表）
 * 创建日期: 2026-07-20
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class LisPacsReportServiceImpl implements LisPacsReportService {

    @Resource
    private LisPacsReportMapper lisPacsReportMapper;

    @Override
    public Long createLisPacsReport(Object saveReqVO) {
        // TODO 后续替换为 LisPacsReportSaveReqVO
        LisPacsReportDO report = BeanUtils.toBean(saveReqVO, LisPacsReportDO.class);
        lisPacsReportMapper.insert(report);
        return report.getId();
    }

    @Override
    public void updateLisPacsReport(Object saveReqVO) {
        // TODO 后续替换为 LisPacsReportSaveReqVO
        LisPacsReportDO updateObj = BeanUtils.toBean(saveReqVO, LisPacsReportDO.class);
        validateLisPacsReportExists(updateObj.getId());
        lisPacsReportMapper.updateById(updateObj);
    }

    @Override
    public void deleteLisPacsReport(Long id) {
        validateLisPacsReportExists(id);
        lisPacsReportMapper.deleteById(id);
    }

    @Override
    public LisPacsReportDO getLisPacsReport(Long id) {
        return lisPacsReportMapper.selectById(id);
    }

    @Override
    public PageResult<LisPacsReportDO> getLisPacsReportPage(PageParam pageParam) {
        // TODO 后续替换为 LisPacsReportPageReqVO，并增加查询条件
        return lisPacsReportMapper.selectPage(pageParam, null);
    }

    @Override
    public LisPacsReportDO fetchFromExternal(String reportNo, String sourceSystem) {
        // TODO 对接 LIS/PACS 系统：根据报告单号从外部系统拉取报告数据，解析后落库
        log.info("[fetchFromExternal] 对接 LIS/PACS 拉取报告 reportNo={}, sourceSystem={}", reportNo, sourceSystem);
        // 先查本地是否已存在
        return lisPacsReportMapper.selectByReportNo(reportNo);
    }

    @Override
    public boolean autoFillToMedicalRecord(Long reportId, Long recordId) {
        // TODO 实现自动填充：解析 reportDataJson，按字段映射规则写入 medical_record_item 表，并更新病历的 aux_exam 字段
        log.info("[autoFillToMedicalRecord] LIS/PACS 报告填充到病历 reportId={}, recordId={}", reportId, recordId);
        validateLisPacsReportExists(reportId);
        return false;
    }

    private void validateLisPacsReportExists(Long id) {
        if (id == null || lisPacsReportMapper.selectById(id) == null) {
            throw exception(LIS_PACS_REPORT_NOT_EXISTS);
        }
    }

}
