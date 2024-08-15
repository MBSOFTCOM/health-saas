package cn.iocoder.yudao.module.ppd.service.screendistrict;

import cn.iocoder.yudao.module.ppd.controller.admin.screendistrict.vo.ScreenDistrictRespVO;
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
     * 获取 所有区划数据
     */
    List<ScreenDistrictRespVO> getAll();


    /**
     * 根据区划级别、父级区划编码获取 区划列表
     */
    List<ScreenDistrictDO> getDistrictList(Integer level, String parentCode);

    /**
     * 根据部门id，获取所在区划编码
     */
    String getDistrictCode(Long deptId);

    List<ScreenDistrictDO> getDistrictList2();

    List<String> getDeptList(List<String> codeList);
}