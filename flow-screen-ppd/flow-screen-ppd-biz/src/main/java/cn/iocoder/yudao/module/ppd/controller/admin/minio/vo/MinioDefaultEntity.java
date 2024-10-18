package cn.iocoder.yudao.module.ppd.controller.admin.minio.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "common-set.minio")
public class MinioDefaultEntity {
    /**
     * 基础路由
     */

    private String endPoint;

    private String accessKey;

    private String secretKey;


}
