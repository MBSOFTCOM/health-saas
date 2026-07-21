package cn.iocoder.yudao.module.childhealth.dal.mysql.spine;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.spine.SpineExamRecordDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 脊柱骨骼筛查 Mapper
 */
@Mapper
public interface SpineExamRecordMapper extends BaseMapperX<SpineExamRecordDO> {

    /**
     * 分页查询脊柱骨骼筛查记录（支持按 examId/childId/riskLevel 查询）
     *
     * @param pageParam 分页参数
     * @param examId    体检记录ID
     * @param childId   儿童ID
     * @param riskLevel 风险分级：1正常 / 2预警 / 3异常
     * @return 分页结果
     */
    default PageResult<SpineExamRecordDO> selectPage(PageParam pageParam, Long examId, Long childId, Integer riskLevel) {
        return selectPage(pageParam, new LambdaQueryWrapperX<SpineExamRecordDO>()
                .eqIfPresent(SpineExamRecordDO::getExamId, examId)
                .eqIfPresent(SpineExamRecordDO::getChildId, childId)
                .eqIfPresent(SpineExamRecordDO::getRiskLevel, riskLevel)
                .orderByDesc(SpineExamRecordDO::getId));
    }

    /**
     * 按体检记录ID查询
     */
    default SpineExamRecordDO selectByExamId(Long examId) {
        return selectOne(SpineExamRecordDO::getExamId, examId);
    }

    /**
     * 按儿童ID查询列表
     */
    default java.util.List<SpineExamRecordDO> selectListByChildId(Long childId) {
        return selectList(new LambdaQueryWrapperX<SpineExamRecordDO>()
                .eqIfPresent(SpineExamRecordDO::getChildId, childId)
                .orderByDesc(SpineExamRecordDO::getExamDate));
    }
}
