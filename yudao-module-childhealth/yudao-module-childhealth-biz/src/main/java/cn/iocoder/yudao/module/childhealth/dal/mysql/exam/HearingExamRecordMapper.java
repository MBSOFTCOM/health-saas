package cn.iocoder.yudao.module.childhealth.dal.mysql.exam;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.exam.HearingExamRecordDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 听力检查 Mapper
 */
@Mapper
public interface HearingExamRecordMapper extends BaseMapperX<HearingExamRecordDO> {
}