package cn.iocoder.yudao.module.ppd.service.minio;

import cn.iocoder.yudao.module.ppd.controller.admin.minio.vo.MinioDefaultEntity;
import io.minio.MinioClient;
import io.minio.errors.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

public interface MinioService {

    final Integer DEFAULT_EXRTIME= 60*60;
    final String ENDPOINT="http://172.16.1.139:9000";
    final String ACCESSKEY="jZ4nUzvZtQZju2rvWyKZ";
    final String SECRETKEY="0vFdJmU1JfVtjibh54QHHX9iIJjfLR4u8w3e077k";
    final String BUCKETNAME="screen-ppd";

    String getPresignedUrl(String endpoint,String accessKey,String secretKey,String bucketName,String objectName);

    Map<String ,String> getPresignedUrl(String endpoint, String accessKey, String secretKey, String bucketName, Map<String, String> objects, Integer exrTime);

    List<String> getPresignedUrl(String endpoint, String accessKey, String secretKey, String bucketName, List<String> objectName, Integer exrTime);

    String getPresignedUrl(String endpoint, String accessKey, String secretKey, String bucketName, String objectName, Integer exrTime) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

    MinioClient minioClient(String endpoint, String accessKey, String secretKey);
    MinioClient minioClient(MinioDefaultEntity minioEntity);


}
