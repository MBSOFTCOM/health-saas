package cn.iocoder.yudao.module.ppd.controller.admin.screenpersonrealsituation.vo;

import cn.iocoder.yudao.module.ppd.controller.admin.screendiagnosis.vo.TBHealthScreening;
import lombok.Data;

import java.util.List;

@Data
public class PatientInfoList {

    /**
     * CT、DR组数据
     */
    private List<ChestRadiographVO> CTDRList;
    /**
     * 采集组数据
     */
    private List<CollectVO> checkList;
    /**
     * 诊断组数据
     */
    private List<DiagnosisVO> diagnoList;
    /**
     * PPD组数据
     */
    private List<ScreenTstVO> PPDList;
    /**
     * 痰检组数据
     */
    private List<SputumExaminationVO> sputumList;
    /**
     * 心电图组数据
     */
    private List<ElectrocardiogramVO> electList;

    /**
     * 体检表数据
     */
    private TBHealthScreening tbHealthScreening;
}
