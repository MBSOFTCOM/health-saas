package cn.iocoder.yudao.module.childhealth.dal.mysql.exam;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.exam.ExamRecordDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 体检记录 Mapper
 */
@Mapper
public interface ExamRecordMapper extends BaseMapperX<ExamRecordDO> {
}