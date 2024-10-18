package cn.iocoder.yudao.module.ppd.service.minio;

import cn.iocoder.yudao.module.ppd.controller.admin.minio.vo.MinioDefaultEntity;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import io.minio.http.Method;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Service
public class MinioServiceImpl implements MinioService{

    @Override
    public String getPresignedUrl(String endpoint, String accessKey, String secretKey, String bucketName, String objectName) {
        return getPresignedUrl(endpoint, accessKey, secretKey, bucketName, objectName,null);
    }
    @Override
    public Map<String ,String> getPresignedUrl(String endpoint, String accessKey, String secretKey, String bucketName, Map<String, String> objects, Integer exrTime) {
        Map<String, String> presignedUrls = new HashMap<>();
        if (exrTime==null){
            exrTime=DEFAULT_EXRTIME;
        }
        MinioClient minioClient = minioClient(endpoint, accessKey, secretKey);
        for (Map.Entry<String, String> entry : objects.entrySet()) {
            String key = entry.getKey();
            String objectName = entry.getValue();
            try {
                GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(bucketName)
                        .object(objectName)
                        .expiry(exrTime) // 过期时间，单位为秒
                        .build();

                String url = minioClient.getPresignedObjectUrl(args);
                presignedUrls.put(key, url);
            } catch (Exception e) {
                e.printStackTrace();
                presignedUrls.put(key, "Error generating URL for " + objectName);
            }
        }
        return presignedUrls;
    }
    @Override
    public List<String> getPresignedUrl(String endpoint, String accessKey, String secretKey, String bucketName, List<String> objectName, Integer exrTime) {
        if (exrTime==null){
            exrTime=DEFAULT_EXRTIME;
        }
        MinioClient minioClient = minioClient(endpoint, accessKey, secretKey);
        Integer finalExrTime = exrTime;
        return objectName.stream().map(name -> {
            try {
                GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(bucketName)
                        .object(name)
                        .expiry(finalExrTime)
                        .build();
                return minioClient.getPresignedObjectUrl(args);
            } catch (ServerException e) {
                throw new RuntimeException(e);
            } catch (InsufficientDataException e) {
                throw new RuntimeException(e);
            } catch (ErrorResponseException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            } catch (InvalidKeyException e) {
                throw new RuntimeException(e);
            } catch (InvalidResponseException e) {
                throw new RuntimeException(e);
            } catch (XmlParserException e) {
                throw new RuntimeException(e);
            } catch (InternalException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }

    @Override
    public String getPresignedUrl(String endpoint, String accessKey, String secretKey, String bucketName, String objectName, Integer exrTime){
        if (exrTime==null){
            exrTime= 60 * 60;
        }
        try {
            MinioClient minioClient = minioClient(endpoint, accessKey, secretKey);
            // 使用 GetPresignedObjectUrlArgs 构建参数
            GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucketName)
                    .object(objectName)
                    .expiry(exrTime) // 过期时间，单位为秒
                    .build();
            // 生成预签名 URL
            return minioClient.getPresignedObjectUrl(args);
        } catch (ErrorResponseException e) {
            throw new RuntimeException(e);
        } catch (InsufficientDataException e) {
            throw new RuntimeException(e);
        } catch (InternalException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (InvalidResponseException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (XmlParserException e) {
            throw new RuntimeException(e);
        } catch (ServerException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public MinioClient minioClient(String endpoint, String accessKey, String secretKey){
        MinioClient minioClient =
                MinioClient.builder()
                        .endpoint(endpoint)
                        .credentials(accessKey, secretKey)
                        .build();
        return minioClient;
    }

    @Override
    public MinioClient minioClient(MinioDefaultEntity minioEntity) {
        return MinioClient.builder()
                .endpoint(minioEntity.getEndPoint())
                .credentials(minioEntity.getAccessKey(), minioEntity.getSecretKey())
                .build();
    }
}
