package cn.iocoder.yudao.module.childhealth.dal.mysql.workflow;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.workflow.HealthCheckupDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HealthCheckupMapper extends BaseMapperX<HealthCheckupDO> {}
