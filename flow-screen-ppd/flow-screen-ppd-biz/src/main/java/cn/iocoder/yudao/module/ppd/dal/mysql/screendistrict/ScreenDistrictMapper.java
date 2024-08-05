package cn.iocoder.yudao.module.ppd.dal.mysql.screendistrict;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screendistrict.ScreenDistrictDO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 甘孜州区划 Mapper
 *
 * @author 侯卿
 */
@Mapper
@InterceptorIgnore(tenantLine = "true")
public interface ScreenDistrictMapper extends BaseMapperX<ScreenDistrictDO> {

    /**
     * 获取 省 名称 列表
     */
    List<ScreenDistrictDO> getProvinceName2();


    /**
     * 获取 市 名称 列表
     */
    List<ScreenDistrictDO> getCityName2();

    /**
     * 获取 县 名称 列表
     */
    List<ScreenDistrictDO> getCountyName2();

    /**
     * 获取 乡镇 名称 列表
     */
    List<ScreenDistrictDO> getTownName2();

    /**
     * 获取 村 名称 列表
     */
    List<ScreenDistrictDO> getVillageName2();

    /**
     * 获取 省 名称 列表
     */
    List<String> getProvinceName();


    /**
     * 获取 市 名称 列表
     */
    List<String> getCityName();

    /**
     * 获取 县 名称 列表
     */
    List<String> getCountyName();

    /**
     * 获取 乡镇 名称 列表
     */
    List<String> getTownName();

    /**
     * 获取 村 名称 列表
     */
    List<String> getVillageName();

    String selectByName(String name);
}