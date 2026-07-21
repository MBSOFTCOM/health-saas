package cn.iocoder.yudao.module.childhealth.dal.mysql.exam;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.exam.ExamAppointmentDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 体检预约 Mapper
 */
@Mapper
public interface ExamAppointmentMapper extends BaseMapperX<ExamAppointmentDO> {
}