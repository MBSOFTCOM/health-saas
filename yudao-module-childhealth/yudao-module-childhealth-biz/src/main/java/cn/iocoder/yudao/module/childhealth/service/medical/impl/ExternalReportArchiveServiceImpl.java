package cn.iocoder.yudao.module.childhealth.service.medical.impl;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.medical.ExternalReportArchiveDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.medical.ExternalReportArchiveMapper;
import cn.iocoder.yudao.module.childhealth.service.medical.ExternalReportArchiveService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.EXTERNAL_REPORT_ARCHIVE_NOT_EXISTS;

/**
 * 外部报告归档表 Service 实现类
 *
 * 模块: A. 儿童基础健康检查（A5-外部报告归档表）
 * 创建日期: 2026-07-20
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class ExternalReportArchiveServiceImpl implements ExternalReportArchiveService {

    @Resource
    private ExternalReportArchiveMapper externalReportArchiveMapper;

    @Override
    public Long createExternalReportArchive(Object saveReqVO) {
        // TODO 后续替换为 ExternalReportArchiveSaveReqVO
        ExternalReportArchiveDO archive = BeanUtils.toBean(saveReqVO, ExternalReportArchiveDO.class);
        externalReportArchiveMapper.insert(archive);
        return archive.getId();
    }

    @Override
    public void updateExternalReportArchive(Object saveReqVO) {
        // TODO 后续替换为 ExternalReportArchiveSaveReqVO
        ExternalReportArchiveDO updateObj = BeanUtils.toBean(saveReqVO, ExternalReportArchiveDO.class);
        validateExternalReportArchiveExists(updateObj.getId());
        externalReportArchiveMapper.updateById(updateObj);
    }

    @Override
    public void deleteExternalReportArchive(Long id) {
        validateExternalReportArchiveExists(id);
        externalReportArchiveMapper.deleteById(id);
    }

    @Override
    public ExternalReportArchiveDO getExternalReportArchive(Long id) {
        return externalReportArchiveMapper.selectById(id);
    }

    @Override
    public PageResult<ExternalReportArchiveDO> getExternalReportArchivePage(PageParam pageParam) {
        // TODO 后续替换为 ExternalReportArchivePageReqVO，并增加查询条件
        return externalReportArchiveMapper.selectPage(pageParam, null);
    }

    @Override
    public Long archiveReport(Long childId, String fileUrl, String reportType, String reportName) {
        // TODO 归档外部报告：可扩展文件格式识别、文件大小记录、来源机构登记等
        log.info("[archiveReport] 归档外部报告 childId={}, fileUrl={}, reportType={}, reportName={}",
                childId, fileUrl, reportType, reportName);
        ExternalReportArchiveDO archive = ExternalReportArchiveDO.builder()
                .childId(childId)
                .fileUrl(fileUrl)
                .reportType(reportType)
                .reportName(reportName)
                .archiveDate(LocalDate.now())
                .build();
        externalReportArchiveMapper.insert(archive);
        return archive.getId();
    }

    private void validateExternalReportArchiveExists(Long id) {
        if (id == null || externalReportArchiveMapper.selectById(id) == null) {
            throw exception(EXTERNAL_REPORT_ARCHIVE_NOT_EXISTS);
        }
    }

}
