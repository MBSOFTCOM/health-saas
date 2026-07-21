package cn.iocoder.yudao.module.childhealth.dal.mysql.device;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.device.RecheckCheckinDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 复筛报到登记表 Mapper
 *
 * 模块: D. 移动端功能补全
 * 创建日期: 2026-07-20
 */
@Mapper
public interface RecheckCheckinMapper extends BaseMapperX<RecheckCheckinDO> {

    /**
     * 按报到编号查询
     */
    default RecheckCheckinDO selectByCheckinNo(String checkinNo) {
        return selectOne(RecheckCheckinDO::getCheckinNo, checkinNo);
    }

    /**
     * 按扫码内容查询
     */
    default RecheckCheckinDO selectByQrcodeContent(String qrcodeContent) {
        return selectOne(RecheckCheckinDO::getQrcodeContent, qrcodeContent);
    }

    /**
     * 按复筛记录ID查询
     */
    default RecheckCheckinDO selectByRecheckId(Long recheckId) {
        return selectOne(RecheckCheckinDO::getRecheckId, recheckId);
    }

    /**
     * 按儿童ID查询报到记录列表
     */
    default List<RecheckCheckinDO> selectListByChildId(Long childId) {
        return selectList(new LambdaQueryWrapperX<RecheckCheckinDO>()
                .eqIfPresent(RecheckCheckinDO::getChildId, childId)
                .orderByDesc(RecheckCheckinDO::getCheckinTime));
    }

    /**
     * 按现场状态查询报到记录列表
     */
    default List<RecheckCheckinDO> selectListByOnSiteStatus(Integer onSiteStatus) {
        return selectList(new LambdaQueryWrapperX<RecheckCheckinDO>()
                .eqIfPresent(RecheckCheckinDO::getOnSiteStatus, onSiteStatus)
                .orderByAsc(RecheckCheckinDO::getCheckinTime));
    }

    /**
     * 按接诊医生ID查询报到记录列表
     */
    default List<RecheckCheckinDO> selectListByReceiveDoctorId(Long receiveDoctorId) {
        return selectList(new LambdaQueryWrapperX<RecheckCheckinDO>()
                .eqIfPresent(RecheckCheckinDO::getReceiveDoctorId, receiveDoctorId)
                .orderByDesc(RecheckCheckinDO::getReceiveTime));
    }

}
