package cn.iocoder.yudao.module.childhealth.dal.mysql.vaccine;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.vaccine.VaccinePlanDO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

/**
 * 疫苗接种计划 Mapper
 */
@Mapper
public interface VaccinePlanMapper extends BaseMapperX<VaccinePlanDO> {

    /**
     * 分页查询疫苗接种计划（支持按 childId/vaccineName/status 查询）
     *
     * @param pageParam    分页参数
     * @param childId      儿童ID
     * @param vaccineName  疫苗名称
     * @param status       计划状态：PENDING/COMPLETED/EXPIRED/SKIPPED
     * @return 分页结果
     */
    default PageResult<VaccinePlanDO> selectPage(PageParam pageParam, Long childId, String vaccineName, String status) {
        return selectPage(pageParam, new LambdaQueryWrapperX<VaccinePlanDO>()
                .eqIfPresent(VaccinePlanDO::getChildId, childId)
                .likeIfPresent(VaccinePlanDO::getVaccineName, vaccineName)
                .eqIfPresent(VaccinePlanDO::getStatus, status)
                .orderByAsc(VaccinePlanDO::getScheduledDate));
    }

    /**
     * 按儿童ID查询所有计划
     */
    default List<VaccinePlanDO> selectListByChildId(Long childId) {
        return selectList(new LambdaQueryWrapperX<VaccinePlanDO>()
                .eqIfPresent(VaccinePlanDO::getChildId, childId)
                .orderByAsc(VaccinePlanDO::getScheduledDate));
    }

    /**
     * 查询未来指定天数内到期的待接种计划
     *
     * @param startDate 起始日期
     * @param endDate   截止日期
     * @return 计划列表
     */
    default List<VaccinePlanDO> selectListByScheduledDateRange(LocalDate startDate, LocalDate endDate) {
        return selectList(new LambdaQueryWrapperX<VaccinePlanDO>()
                .eqIfPresent(VaccinePlanDO::getStatus, "PENDING")
                .betweenIfPresent(VaccinePlanDO::getScheduledDate, startDate, endDate)
                .orderByAsc(VaccinePlanDO::getScheduledDate));
    }

    /**
     * 按儿童ID和疫苗编码查询计划，用于去重判断
     */
    default VaccinePlanDO selectByChildIdAndVaccineCode(Long childId, String vaccineCode, Integer doseNo) {
        return selectOne(new LambdaQueryWrapperX<VaccinePlanDO>()
                .eqIfPresent(VaccinePlanDO::getChildId, childId)
                .eqIfPresent(VaccinePlanDO::getVaccineCode, vaccineCode)
                .eqIfPresent(VaccinePlanDO::getDoseNo, doseNo));
    }
}