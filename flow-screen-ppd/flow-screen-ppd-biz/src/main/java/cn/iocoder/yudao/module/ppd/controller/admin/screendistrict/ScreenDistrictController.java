package cn.iocoder.yudao.module.ppd.controller.admin.screendistrict;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screendistrict.ScreenDistrictDO;
import cn.iocoder.yudao.module.ppd.service.screendistrict.ScreenDistrictService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 甘孜州区划")
@RestController
@RequestMapping("/tb/screen-district")
@Validated
public class ScreenDistrictController {

    @Resource
    private ScreenDistrictService screenDistrictService;


    @GetMapping("/get-province")
    public CommonResult<List<ScreenDistrictDO>> getProvince(){
        List<ScreenDistrictDO> provinceName = screenDistrictService.getProvinceName2();
        return success(provinceName);
    }

    @GetMapping("/get-city")
    public CommonResult<List<ScreenDistrictDO>> getCity(@RequestParam("provinceCode") String provinceCode){
        List<ScreenDistrictDO> cityName = screenDistrictService.getCityName2(provinceCode);
        return success(cityName);
    }


    @GetMapping("/get-county")
    public CommonResult<List<ScreenDistrictDO>> getCounty(@RequestParam("cityCode") String cityCode){
        List<ScreenDistrictDO> countyName = screenDistrictService.getCountyName2(cityCode);
        return success(countyName);
    }



    @GetMapping("/get-town")
    public CommonResult<List<ScreenDistrictDO>> getTown(@RequestParam("countyCode") String countyCode){
        List<ScreenDistrictDO> townName = screenDistrictService.getTownName2(countyCode);
        return success(townName);
    }



    @GetMapping("/get-village")
    public CommonResult<List<ScreenDistrictDO>> getVillage(){
        List<ScreenDistrictDO> villageName = screenDistrictService.getVillageName2();
        return success(villageName);
    }

}