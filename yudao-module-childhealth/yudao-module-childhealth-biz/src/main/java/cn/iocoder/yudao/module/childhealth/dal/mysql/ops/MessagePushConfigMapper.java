package cn.iocoder.yudao.module.childhealth.dal.mysql.ops;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.ops.MessagePushConfigDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 消息推送配置Mapper
 */
@Mapper
public interface MessagePushConfigMapper extends BaseMapperX<MessagePushConfigDO> {
}