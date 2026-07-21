package cn.iocoder.yudao.module.childhealth.service.external;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * 外部系统对接配置
 *
 * 控制各对接适配器的 Mock/真实实现切换。
 *
 * 配置示例（application.yaml）：
 *
 * childhealth:
 *   external:
 *     his:
 *       mode: mock           # mock / real
 *       endpoint: https://his.hospital.local/api
 *     obstetrics:
 *       mode: mock
 *       endpoint: https://obs.hospital.local/api
 *     lis:
 *       mode: mock
 *       endpoint: https://lis.hospital.local/api
 *     pacs:
 *       mode: mock
 *       endpoint: https://pacs.hospital.local/api
 *     device:
 *       mode: mock
 *       default-port: COM3
 *
 * 部署时将各 mode 改为 real 即可切换到真实实现（需提供对应的 Adapter 实现 Bean）。
 *
 * 当前所有适配器默认 matchIfMissing=true，即未配置时使用 Mock。
 */
@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "childhealth.external")
public class ExternalSystemConfig {

    /**
     * HIS 系统配置
     */
    private SystemConfig his = new SystemConfig();

    /**
     * 产科系统配置
     */
    private SystemConfig obstetrics = new SystemConfig();

    /**
     * LIS 检验系统配置
     */
    private SystemConfig lis = new SystemConfig();

    /**
     * PACS 影像系统配置
     */
    private SystemConfig pacs = new SystemConfig();

    /**
     * 设备对接配置
     */
    private DeviceConfig device = new DeviceConfig();

    /**
     * 单个系统配置
     */
    @Data
    public static class SystemConfig {
        /**
         * 模式：mock / real
         */
        private String mode = "mock";

        /**
         * 端点 URL
         */
        private String endpoint;

        /**
         * 超时（毫秒）
         */
        private Integer timeout = 5000;

        /**
         * 鉴权 Token
         */
        private String authToken;
    }

    /**
     * 设备对接配置（扩展自系统配置）
     */
    @Data
    public static class DeviceConfig extends SystemConfig {
        /**
         * 默认串口（如 COM3 / /dev/ttyUSB0）
         */
        private String defaultPort;

        /**
         * 默认波特率
         */
        private Integer defaultBaudRate = 9600;
    }

    @PostConstruct
    public void logConfig() {
        Map<String, String> status = new HashMap<>();
        status.put("HIS", his.getMode());
        status.put("Obstetrics", obstetrics.getMode());
        status.put("LIS", lis.getMode());
        status.put("PACS", pacs.getMode());
        status.put("Device", device.getMode());
        log.info("[ExternalSystemConfig] 外部系统对接模式: {}", status);
    }

}
