package cn.iocoder.yudao.module.ppd.service.screenimages;


import cn.hutool.core.io.IoUtil;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.infra.api.file.FileApi;
import cn.iocoder.yudao.module.ppd.controller.admin.screenimages.vo.ScreenImagesSaveReqVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenimages.ScreenImagesDO;
import cn.iocoder.yudao.module.ppd.dal.mysql.screenimages.ScreenImagesMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.InputStream;
import java.util.List;

/**
 * 移动端各组离线图片信息 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ScreenImagesServiceImpl implements ScreenImagesService {

    @Resource
    private FileApi fileApi;
    @Resource
    private ScreenImagesMapper screenImagesMapper;

    @Override
    public String updateImage(InputStream inputStream) {
        // 存储文件
        return fileApi.createFile(IoUtil.readBytes(inputStream));
    }

    @Override
    public Long createScreenImages(ScreenImagesSaveReqVO createReqVO) {
        // 插入
        ScreenImagesDO screenImages = BeanUtils.toBean(createReqVO, ScreenImagesDO.class);

        // 判断插入是否重复
        List<ScreenImagesDO> screenImagesDOS = screenImagesMapper.selectList(new LambdaQueryWrapperX<ScreenImagesDO>()
//                .eq(ScreenImagesDO::getScreenId, screenImages.getScreenId())
//                .eq(ScreenImagesDO::getPersonId, screenImages.getPersonId())
                .eq(ScreenImagesDO::getType, screenImages.getType())
                .eq(ScreenImagesDO::getIdNum, screenImages.getIdNum())
//                .eq(ScreenImagesDO::getPath, screenImages.getPath())
//                .eq(ScreenImagesDO::getUrl, screenImages.getUrl())
                .eq(ScreenImagesDO::getScreenOrder, screenImages.getScreenOrder())
//                .eq(ScreenImagesDO::getScreenPoint, screenImages.getScreenPoint())
                .eq(ScreenImagesDO::getYear, screenImages.getYear())
                .eq(ScreenImagesDO::getScreenType, screenImages.getScreenType())
        );
        // 重复插入则返回-1
        if(screenImagesDOS != null && screenImagesDOS.size() > 0){
            screenImages.setId(screenImagesDOS.get(0).getId());
            screenImagesMapper.updateById(screenImages);
            return -1L;
        }
        screenImagesMapper.insert(screenImages);
        // 返回
        return screenImages.getId();
    }

}