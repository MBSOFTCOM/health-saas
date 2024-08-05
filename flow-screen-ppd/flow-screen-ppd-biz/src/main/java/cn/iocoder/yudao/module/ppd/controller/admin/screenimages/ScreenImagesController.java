package cn.iocoder.yudao.module.ppd.controller.admin.screenimages;


import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.ppd.controller.admin.screenimages.vo.ScreenImagesSaveReqVO;
import cn.iocoder.yudao.module.ppd.service.screenimages.ScreenImagesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.module.infra.enums.ErrorCodeConstants.FILE_IS_EMPTY;

@Tag(name = "管理后台 - 移动端各组离线图片信息")
@RestController
@RequestMapping("/tb/screen-images")
@Validated
public class ScreenImagesController {
    @Resource
    private ScreenImagesService screenImagesService;

    /**
     * 上传离线图片
     * @param file 离线图片文件
     * @return 图片的地址
     * @throws Exception IO异常
     */
    @RequestMapping(value = "/updateImage",
            method = {RequestMethod.POST, RequestMethod.PUT}) // 解决 uni-app 不支持 Put 上传文件的问题
    @Operation(summary = "上传离线图片并保存图片")
    public CommonResult<String> updateUserAvatar(@RequestParam("imageFile") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw exception(FILE_IS_EMPTY);
        }
        String imageUrl = screenImagesService.updateImage(file.getInputStream());
        return success(imageUrl);
    }

    @PostMapping("/create")
    @Operation(summary = "创建移动端各组离线图片信息")
    public CommonResult<Long> createScreenImages(@Valid @RequestBody ScreenImagesSaveReqVO createReqVO) {
        return success(screenImagesService.createScreenImages(createReqVO));
    }
}