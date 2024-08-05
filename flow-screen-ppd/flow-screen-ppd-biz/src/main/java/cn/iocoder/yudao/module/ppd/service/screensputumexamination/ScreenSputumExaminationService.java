package cn.iocoder.yudao.module.ppd.service.screensputumexamination;


import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.ppd.controller.admin.screenexperiment.vo.ScreenExperimentPageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screensputumexamination.vo.ScreenSputumExaminationPageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screensputumexamination.vo.ScreenSputumExaminationSaveReqVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screensputumexamination.ScreenSputumExaminationDO;
import jakarta.validation.Valid;

/**
 * 痰检组 Service 接口
 *
 * @author 芋道源码
 */
public interface ScreenSputumExaminationService {

    /**
     * 创建痰检组
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createScreenSputumExamination(@Valid ScreenSputumExaminationSaveReqVO createReqVO);

    /**
     * 更新痰检组
     *
     * @param updateReqVO 更新信息
     */
    void updateScreenSputumExamination(@Valid ScreenSputumExaminationSaveReqVO updateReqVO);

    /**
     * 删除痰检组
     *
     * @param id 编号
     */
    void deleteScreenSputumExamination(Long id);

    /**
     * 获得痰检组
     *
     * @param id 编号
     * @return 痰检组
     */
    ScreenSputumExaminationDO getScreenSputumExamination(Long id);

    /**
     * 获得痰检组分页
     *
     * @param pageReqVO 分页查询
     * @return 痰检组分页
     */
    PageResult<ScreenSputumExaminationDO> getScreenSputumExaminationPage(ScreenSputumExaminationPageReqVO pageReqVO);

    /**
     * 获取实验组分页数据 痰检组和摸底表进行关联查询得出分页结果
     * @param pageReqVO 分页请求参数
     * @return 关联查询得出的痰检组分页结果
     */
    PageResult<ScreenSputumExaminationDO> getScreenSputumExaminationPageJoinPerson(ScreenExperimentPageReqVO pageReqVO);
}