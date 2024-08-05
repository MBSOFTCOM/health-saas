package cn.iocoder.yudao.module.ppd.service.screendiagnosis;


import cn.iocoder.yudao.module.ppd.controller.admin.screendiagnosis.vo.TBHealthScreening;

/**
 * 诊断组 Service 接口
 *
 * @author 芋道源码
 */
public interface ScreenDiagnosisService {



    /**
     * 根据摸底表id 获取到对应体检表中需要回显的数据
     * @param personId 摸底表id
     * @return 对象
     */
    TBHealthScreening getTbHealthScreening(Long personId, Integer year, Integer screenType);

}