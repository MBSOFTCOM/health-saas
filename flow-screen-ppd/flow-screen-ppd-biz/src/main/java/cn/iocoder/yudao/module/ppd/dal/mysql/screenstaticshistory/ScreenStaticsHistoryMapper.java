package cn.iocoder.yudao.module.ppd.dal.mysql.screenstaticshistory;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.ppd.controller.admin.screenstaticshistory.vo.ScreenStaticsHistoryPageReqVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenstaticshistory.ScreenStaticsHistoryDO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Mapper;

/**
 * 工作进展报告-统计表-导出的历史选项 Mapper
 *
 * @author 福乐云
 */
@Mapper
@InterceptorIgnore(tenantLine = "true")
public interface ScreenStaticsHistoryMapper extends BaseMapperX<ScreenStaticsHistoryDO> {

    default PageResult<ScreenStaticsHistoryDO> selectPage(ScreenStaticsHistoryPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ScreenStaticsHistoryDO>()
                .eqIfPresent(ScreenStaticsHistoryDO::getDeptId, reqVO.getDeptId())
                .eqIfPresent(ScreenStaticsHistoryDO::getTableTittle, reqVO.getTableTittle())
                .eqIfPresent(ScreenStaticsHistoryDO::getSchool, reqVO.getSchool())
                .eqIfPresent(ScreenStaticsHistoryDO::getHospital, reqVO.getHospital())
                .eqIfPresent(ScreenStaticsHistoryDO::getDistrict, reqVO.getDistrict())
                .eqIfPresent(ScreenStaticsHistoryDO::getContact, reqVO.getContact())
                .eqIfPresent(ScreenStaticsHistoryDO::getContactPhone, reqVO.getContactPhone())
                .eqIfPresent(ScreenStaticsHistoryDO::getInjectionPeople, reqVO.getInjectionPeople())
                .eqIfPresent(ScreenStaticsHistoryDO::getCheckPeople, reqVO.getCheckPeople())
                .eqIfPresent(ScreenStaticsHistoryDO::getInfoList, reqVO.getInfoList())
                .betweenIfPresent(ScreenStaticsHistoryDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ScreenStaticsHistoryDO::getId));
    }

    /**
     * 根据机构查询历史选项
     * @param deptId 机构id
     * @return 历史选项配置
     */
    default ScreenStaticsHistoryDO selectOneByDept(Long deptId){
        return selectOne(new LambdaQueryWrapperX<ScreenStaticsHistoryDO>()
                .eqIfPresent(ScreenStaticsHistoryDO::getDeptId,deptId)
                .eqIfPresent(ScreenStaticsHistoryDO::getDeleted,0)
        );
    }

}