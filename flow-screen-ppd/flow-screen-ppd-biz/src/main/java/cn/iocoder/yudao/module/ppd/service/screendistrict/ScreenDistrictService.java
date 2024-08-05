package cn.iocoder.yudao.module.ppd.service.screendistrict;

import cn.iocoder.yudao.module.ppd.dal.dataobject.screendistrict.ScreenDistrictDO;

import java.util.List;

/**
 * 甘孜州区划 Service 接口
 *
 * @author 侯卿
 */
public interface ScreenDistrictService {

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




    /**
     * 获取 省 名称 列表
     */
    List<ScreenDistrictDO> getProvinceName2();


    /**
     * 获取 市 名称 列表
     */
    List<ScreenDistrictDO> getCityName2(String provinceCode);

    /**
     * 获取 县 名称 列表
     */
    List<ScreenDistrictDO> getCountyName2(String cityCode);

    /**
     * 获取 乡镇 名称 列表
     */
    List<ScreenDistrictDO> getTownName2(String countyCode);

    /**
     * 获取 村 名称 列表
     */
    List<ScreenDistrictDO> getVillageName2();



}