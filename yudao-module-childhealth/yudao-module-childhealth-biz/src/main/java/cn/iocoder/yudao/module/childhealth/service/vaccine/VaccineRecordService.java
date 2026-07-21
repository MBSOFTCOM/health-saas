package cn.iocoder.yudao.module.childhealth.service.vaccine;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.vaccine.VaccineRecordDO;

/**
 * 疫苗接种记录 Service 接口
 */
public interface VaccineRecordService {

    /**
     * 分页查询疫苗接种记录
     */
    PageResult<VaccineRecordDO> getVaccineRecordPage(PageParam pageParam, Long childId, String vaccineName, String status);

    /**
     * 根据 ID 查询接种记录
     */
    VaccineRecordDO getVaccineRecord(Long id);

    /**
     * 新增接种记录
     */
    Long createVaccineRecord(VaccineRecordDO record);

    /**
     * 修改接种记录
     */
    void updateVaccineRecord(VaccineRecordDO record);

    /**
     * 删除接种记录
     */
    void deleteVaccineRecord(Long id);

    /**
     * 接种执行确认：新增接种记录，并同步更新关联计划状态为 COMPLETED
     *
     * @param record 接种记录（应包含 vaccinePlanId）
     * @return 接种记录主键 ID
     */
    Long confirmInoculation(VaccineRecordDO record);
}
