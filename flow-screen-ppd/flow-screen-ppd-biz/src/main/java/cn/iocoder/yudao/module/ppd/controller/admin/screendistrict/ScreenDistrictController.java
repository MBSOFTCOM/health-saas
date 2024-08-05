package cn.iocoder.yudao.module.ppd.controller.admin.screendistrict;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.ppd.service.screendistrict.ScreenDistrictService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public CommonResult<List<String>> getProvince(){
        List<String> provinceName = screenDistrictService.getProvinceName();
        return success(provinceName);
    }

    @GetMapping("/get-city")
    public CommonResult<List<String>> getCity(){
        List<String> cityName = screenDistrictService.getCityName();
        return success(cityName);
    }


    @GetMapping("/get-county")
    public CommonResult<List<String>> getCounty(){
        List<String> countyName = screenDistrictService.getCountyName();
        return success(countyName);
    }



    @GetMapping("/get-town")
    public CommonResult<List<String>> getTown(){
        List<String> townName = screenDistrictService.getTownName();
        return success(townName);
    }



    @GetMapping("/get-village")
    public CommonResult<List<String>> getVillage(){
        List<String> villageName = screenDistrictService.getVillageName();
        return success(villageName);
    }

}