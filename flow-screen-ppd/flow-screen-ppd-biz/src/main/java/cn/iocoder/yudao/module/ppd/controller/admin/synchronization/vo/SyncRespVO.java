package cn.iocoder.yudao.module.ppd.controller.admin.synchronization.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class SyncRespVO {
    /**
     * 平板上传入的表id
     */
    private Long id;
    /**
     * mysql插入后的新id
     */
    private Long newId;
    /**
     * 身份证
     */
    private String idNum;
    /**
     * 筛查编号
     */
    private String screenId;
}
