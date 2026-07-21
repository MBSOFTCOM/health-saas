package cn.iocoder.yudao.module.childhealth.service.vaccine;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.vaccine.VaccinePlanDO;

import java.util.List;

/**
 * 疫苗接种计划 Service 接口
 */
public interface VaccinePlanService {

    /**
     * 分页查询疫苗接种计划
     *
     * @param pageParam    分页参数
     * @param childId      儿童ID
     * @param vaccineName  疫苗名称
     * @param status       计划状态
     * @return 分页结果
     */
    PageResult<VaccinePlanDO> getVaccinePlanPage(PageParam pageParam, Long childId, String vaccineName, String status);

    /**
     * 根据 ID 查询疫苗接种计划
     *
     * @param id 主键ID
     * @return 疫苗接种计划
     */
    VaccinePlanDO getVaccinePlan(Long id);

    /**
     * 新增疫苗接种计划
     *
     * @param plan 疫苗接种计划
     * @return 主键ID
     */
    Long createVaccinePlan(VaccinePlanDO plan);

    /**
     * 修改疫苗接种计划
     *
     * @param plan 疫苗接种计划
     */
    void updateVaccinePlan(VaccinePlanDO plan);

    /**
     * 删除疫苗接种计划
     *
     * @param id 主键ID
     */
    void deleteVaccinePlan(Long id);

    /**
     * 根据儿童ID自动生成国家免疫规划疫苗接种计划
     * <p>
     * 包含：乙肝疫苗（0/1/6月）、卡介苗（0月）、脊灰疫苗（2/3/4月）、
     * 百白破（3/4/5月）、麻风疫苗（8月）、乙脑减毒（8月/2岁）、
     * A群流脑（6/9月）、A+C群流脑（3岁/6岁）、甲肝减毒（18月）
     *
     * @param childId 儿童ID
     * @return 生成的计划数量
     */
    int generatePlanByChild(Long childId);

    /**
     * 查询未来指定天数内到期的待接种计划
     *
     * @param days 天数
     * @return 计划列表
     */
    List<VaccinePlanDO> getUpcomingPlans(int days);

    /**
     * 发送接种提醒（实际可对接消息推送服务）
     *
     * @param planId 计划ID
     * @return 是否发送成功
     */
    boolean sendReminder(Long planId);
}