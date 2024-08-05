package cn.iocoder.yudao.module.ppd.service.screendistrict;



import cn.iocoder.yudao.module.ppd.dal.dataobject.screendistrict.ScreenDistrictDO;
import cn.iocoder.yudao.module.ppd.dal.mysql.screendistrict.ScreenDistrictMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;


import java.util.List;

/**
 * 甘孜州区划 Service 实现类
 *
 * @author 侯卿
 */
@Service
@Validated
public class ScreenDistrictServiceImpl implements ScreenDistrictService {

    @Resource
    private ScreenDistrictMapper districtMapper;


    @Override
    public List<String> getProvinceName() {
        return districtMapper.getProvinceName();
    }

    @Override
    public List<String> getCityName() {
        return districtMapper.getCityName();
    }

    @Override
    public List<String> getCountyName() {
        return districtMapper.getCountyName();
    }

    @Override
    public List<String> getTownName() {
        return districtMapper.getTownName();
    }

    @Override
    public List<String> getVillageName() {
        return districtMapper.getVillageName();
    }

    @Override
    public List<ScreenDistrictDO> getProvinceName2() {
        return districtMapper.getProvinceName2();
    }

    @Override
    public List<ScreenDistrictDO> getCityName2(String provinceCode) {
        return districtMapper.getCityName2(provinceCode);
    }

    @Override
    public List<ScreenDistrictDO> getCountyName2(String cityCode) {
        return districtMapper.getCountyName2(cityCode);
    }

    @Override
    public List<ScreenDistrictDO> getTownName2(String countyCode) {
        return districtMapper.getTownName2(countyCode);
    }

    @Override
    public List<ScreenDistrictDO> getVillageName2() {
        return districtMapper.getVillageName2();
    }
}