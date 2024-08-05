package cn.iocoder.yudao.module.ppd.service.screendistrict;



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
}