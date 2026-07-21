package cn.iocoder.yudao.module.childhealth.dal.mysql.vaccine;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.vaccine.VaccineRecordDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 疫苗接种记录 Mapper
 */
@Mapper
public interface VaccineRecordMapper extends BaseMapperX<VaccineRecordDO> {

    /**
     * 分页查询疫苗接种记录（支持按 childId/vaccineName/status 查询）
     *
     * @param pageParam    分页参数
     * @param childId      儿童ID
     * @param vaccineName  疫苗名称
     * @param status       接种状态：COMPLETED/INCOMPLETE/DEFERRED
     * @return 分页结果
     */
    default PageResult<VaccineRecordDO> selectPage(PageParam pageParam, Long childId, String vaccineName, String status) {
        return selectPage(pageParam, new LambdaQueryWrapperX<VaccineRecordDO>()
                .eqIfPresent(VaccineRecordDO::getChildId, childId)
                .likeIfPresent(VaccineRecordDO::getVaccineName, vaccineName)
                .eqIfPresent(VaccineRecordDO::getStatus, status)
                .orderByDesc(VaccineRecordDO::getInoculationDate));
    }

    /**
     * 按儿童ID查询所有接种记录
     */
    default List<VaccineRecordDO> selectListByChildId(Long childId) {
        return selectList(new LambdaQueryWrapperX<VaccineRecordDO>()
                .eqIfPresent(VaccineRecordDO::getChildId, childId)
                .orderByDesc(VaccineRecordDO::getInoculationDate));
    }

    /**
     * 按接种计划ID查询接种记录
     */
    default VaccineRecordDO selectByVaccinePlanId(Long vaccinePlanId) {
        return selectOne(VaccineRecordDO::getVaccinePlanId, vaccinePlanId);
    }
}