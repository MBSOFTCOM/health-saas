package cn.iocoder.yudao.module.ppd.controller.admin.minio;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.ppd.controller.admin.minio.vo.MinioDefaultEntity;
import cn.iocoder.yudao.module.ppd.service.minio.MinioService;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;


@Tag(name = "管理后台 - 下载文件")
@RestController
@RequestMapping("/tb/minioFile")
public class MinioController {

    @Resource
    private MinioService minioService;
    @Autowired
    private MinioDefaultEntity minioDefaultEntity;

    @PostMapping("/api/getPresignedUrl")
    public CommonResult<Map<String,String>> getPresignedUrl(@RequestBody Map<String ,String > map) {
        Map<String, String> presignedUrl = minioService.getPresignedUrl(MinioService.ENDPOINT, MinioService.ACCESSKEY, MinioService.SECRETKEY, MinioService.BUCKETNAME, map, MinioService.DEFAULT_EXRTIME);
        return success(presignedUrl);
    }

    @GetMapping("/down/{name}/{bucket}")
    public void downLoadFile(@PathVariable String name,@PathVariable String bucket, HttpServletResponse response) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        MinioClient minioClient = minioService.minioClient(minioDefaultEntity);
        InputStream inputStream = minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucket)
                .object(name)
                .build());
        // 设置响应头
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(name, "UTF-8"));

        // 将文件内容写入响应输出流
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            response.getOutputStream().write(buffer, 0, bytesRead);
        }

        // 关闭输入流
        inputStream.close();
    }
}