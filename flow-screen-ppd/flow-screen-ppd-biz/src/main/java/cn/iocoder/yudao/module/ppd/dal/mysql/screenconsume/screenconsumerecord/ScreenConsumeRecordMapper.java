package cn.iocoder.yudao.module.ppd.dal.mysql.screenconsume.screenconsumerecord;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.ppd.controller.admin.screenconsumerecord.vo.ScreenConsumeRecordPageReqVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenconsumerecord.ScreenConsumeRecordDO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Mapper;

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

}