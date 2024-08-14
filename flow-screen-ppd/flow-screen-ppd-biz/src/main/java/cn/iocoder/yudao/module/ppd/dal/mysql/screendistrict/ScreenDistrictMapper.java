package cn.iocoder.yudao.module.ppd.dal.mysql.screendistrict;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.ppd.controller.admin.screendistrict.vo.ScreenDistrictRespVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screendistrict.ScreenDistrictDO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

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
    List<ScreenDistrictRespVO> selectAll();

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

    String getNameByCode(String code);

    /**
     * 根据区划级别获取 区划列表
     */
    List<ScreenDistrictDO> getDistrictList(@Param("level") Integer level,
                                           @Param("parentCode") String parentCode);

    /**
     * 根据部门id，获取所在区划编码
     */
    String getDistrictCode(Long deptId);
}