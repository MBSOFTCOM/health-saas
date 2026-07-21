package cn.iocoder.yudao.module.childhealth.dal.mysql.exam;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.exam.PhysicalExamRecordDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 体格检查记录 Mapper
 */
@Mapper
public interface PhysicalExamRecordMapper extends BaseMapperX<PhysicalExamRecordDO> {
}