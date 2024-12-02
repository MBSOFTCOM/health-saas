package cn.iocoder.yudao.module.ppd.controller.admin.minio;

import cn.iocoder.yudao.module.ppd.controller.admin.minio.vo.MinioDefaultEntity;
import cn.iocoder.yudao.module.ppd.service.minio.MinioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "管理后台 - 下载文件")
@RestController
@RequestMapping("/tb/minioFile")
public class MinioController {

    @Resource
    private MinioService minioService;
    @Autowired
    private MinioDefaultEntity minioDefaultEntity;
}