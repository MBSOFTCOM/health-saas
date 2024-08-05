package cn.iocoder.yudao.module.ppd.service.screenpersonrealsituation;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.ppd.controller.admin.screenpersonrealsituation.vo.*;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenpersonrealsituation.ScreenPersonDO;
import cn.iocoder.yudao.module.system.api.dict.dto.DictDataRespDTO;
import jakarta.validation.Valid;


import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 摸底 Service 接口
 *
 * @author 侯卿
 */
public interface ScreenPersonService {

    /**
     * 创建摸底
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createScreenPerson(@Valid ScreenPersonSaveReqVO createReqVO);

    /**
     * 更新摸底
     *
     * @param updateReqVO 更新信息
     */
    void updateScreenPerson(@Valid ScreenPersonSaveReqVO updateReqVO);

    /**
     * 删除摸底
     *
     * @param id 编号
     */
    void deleteScreenPerson(Long id);

    /**
     * 获得摸底
     *
     * @param id 编号
     * @return 摸底
     */
    ScreenPersonDO getScreenPerson(Long id);

    /**
     * 获得摸底分页
     *
     * @param pageReqVO 分页查询
     * @return 摸底分页
     */
    PageResult<ScreenPersonDO> getScreenPersonPage(ScreenPersonPageReqVO pageReqVO);


    /**
     * 获得待筛查人员分页
     *
     * @param pageReqVO 分页查询
     * @return 待筛查人员分页
     */
    PageResult<ScreenPersonDO> getScreenedPage(ScreenPersonPageReqVO pageReqVO);


    /**
     * excel 下拉选
     */
    void selectedData(Integer index, List<DictDataRespDTO> dictDataRespDTOS,
                              Map<Integer, List<String>> selectedData);

    /**
     * 导入摸底人员
     */
    ScreenPersonImportRespVO importScreenPerson(List<ScreenPersonImportVO> list, Integer year, Integer screenType, LocalDateTime screenTime);

    /**
     * 摸底人员导入模板
     */
    List<ScreenPersonImportVO> createSampleData();

    /**
     * Excel下拉框数据
     */
    void addSelectedData(String dictType, int index, Map<Integer, List<String>> selectedData);

    /**
     * 导出是对多人群分类的处理
     */
    String resolveMoreTypeToString(Integer moreType);

    /**
     * 根据患者id，获取去各个组的数据
     */
    PatientInfoList getPatientInfoList(Long patientId, Integer year, Integer screenType);

    /**
     * 获取图片、照片的地址
     */
    ImageVO getImageUrl(Long personId, Integer type, String screenId, Integer screenOrder, Integer year, Integer screenType);

    /**
     * 更新照片
     */
    String updateImage(Long imageId, InputStream file);

    /**
     * 更新照片
     */
    String updateImag2(Long personId, String screenId, Integer imageType, Integer screenOrder,Integer year, Integer screenType, InputStream file);


    void resolveDistrict(ScreenPersonDO obj);
}