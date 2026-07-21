package cn.iocoder.yudao.module.childhealth.dal.mysql.exam;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.exam.LabReportDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 辅助检查报告 Mapper
 */
@Mapper
public interface LabReportMapper extends BaseMapperX<LabReportDO> {
}