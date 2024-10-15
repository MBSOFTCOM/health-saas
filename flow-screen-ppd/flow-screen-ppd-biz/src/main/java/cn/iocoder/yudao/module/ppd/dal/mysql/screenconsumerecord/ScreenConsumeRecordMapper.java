package cn.iocoder.yudao.module.ppd.dal.mysql.screenconsumerecord;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.ppd.controller.admin.screenconsumerecord.vo.ScreenConsumeRecordPageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenconsumerecord.vo.ScreenConsumeRecordRespVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenconsumerecord.ScreenConsumeRecordDO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 消耗管理记录 Mapper
 *
 * @author 侯卿
 */
@Mapper
@InterceptorIgnore(tenantLine = "true")
public interface ScreenConsumeRecordMapper extends BaseMapperX<ScreenConsumeRecordDO> {

    default PageResult<ScreenConsumeRecordDO> selectPage(ScreenConsumeRecordPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ScreenConsumeRecordDO>()
                .eqIfPresent(ScreenConsumeRecordDO::getChangeNumber, reqVO.getChangeNumber())
                .eqIfPresent(ScreenConsumeRecordDO::getType, reqVO.getType())
                .betweenIfPresent(ScreenConsumeRecordDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ScreenConsumeRecordDO::getId));
    }

    /**
     * 获取库存记录列表
     */
    List<ScreenConsumeRecordRespVO> getScreenConsumeRecordList(Long id, Integer pageSize, Integer pageNo);
    /**
     * 获取库存记录列表
     */
     Long countScreenConsumeRecordList(Long id);
}