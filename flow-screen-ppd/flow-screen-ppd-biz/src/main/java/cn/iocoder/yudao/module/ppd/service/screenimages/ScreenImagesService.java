package cn.iocoder.yudao.module.ppd.service.screenimages;


import cn.iocoder.yudao.module.ppd.controller.admin.screenimages.vo.ScreenImagesSaveReqVO;
import jakarta.validation.Valid;

import java.io.InputStream;

/**
 * 移动端各组离线图片信息 Service 接口
 *
 * @author 芋道源码
 */
public interface ScreenImagesService {

    String updateImage(InputStream inputStream);

    /**
     * 创建移动端各组离线图片信息
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createScreenImages(@Valid ScreenImagesSaveReqVO createReqVO);
}