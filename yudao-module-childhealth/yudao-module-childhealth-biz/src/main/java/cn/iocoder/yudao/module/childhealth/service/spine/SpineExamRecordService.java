package cn.iocoder.yudao.module.childhealth.service.spine;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.spine.SpineExamRecordDO;

/**
 * 脊柱骨骼筛查 Service 接口
 */
public interface SpineExamRecordService {

    /**
     * 分页查询脊柱骨骼筛查记录
     *
     * @param pageParam 分页参数
     * @param examId    体检记录ID
     * @param childId   儿童ID
     * @param riskLevel 风险分级
     * @return 分页结果
     */
    PageResult<SpineExamRecordDO> getSpineExamRecordPage(PageParam pageParam, Long examId, Long childId, Integer riskLevel);

    /**
     * 根据 ID 查询脊柱骨骼筛查记录
     *
     * @param id 主键ID
     * @return 脊柱骨骼筛查记录
     */
    SpineExamRecordDO getSpineExamRecord(Long id);

    /**
     * 新增脊柱骨骼筛查记录
     *
     * @param record 脊柱骨骼筛查记录
     * @return 主键ID
     */
    Long createSpineExamRecord(SpineExamRecordDO record);

    /**
     * 修改脊柱骨骼筛查记录
     *
     * @param record 脊柱骨骼筛查记录
     */
    void updateSpineExamRecord(SpineExamRecordDO record);

    /**
     * 删除脊柱骨骼筛查记录
     *
     * @param id 主键ID
     */
    void deleteSpineExamRecord(Long id);

    /**
     * 智能评估风险：基于 ATR 角度/Cobb 角/足弓指数自动判定风险分级和诊断建议
     *
     * @param record 脊柱骨骼筛查记录（含检查数据）
     * @return 评估后的脊柱骨骼筛查记录（含风险分级、诊断、建议、矫正方案）
     */
    SpineExamRecordDO autoAssessSpineRisk(SpineExamRecordDO record);
}
