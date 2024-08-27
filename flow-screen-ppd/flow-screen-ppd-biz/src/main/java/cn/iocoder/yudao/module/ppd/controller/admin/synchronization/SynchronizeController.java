package cn.iocoder.yudao.module.ppd.controller.admin.synchronization;


import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.datapermission.core.annotation.DataPermission;
import cn.iocoder.yudao.module.ppd.controller.admin.screenchestradiograph.vo.ScreenChestRadiographPageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenchestradiograph.vo.ScreenChestRadiographRespVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenchestradiograph.vo.ScreenChestRadiographSaveReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screencollect.vo.ScreenCollectPageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screencollect.vo.ScreenCollectRespVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screencollect.vo.ScreenCollectSaveReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenpersonrealsituation.vo.ScreenPersonPageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenpersonrealsituation.vo.ScreenPersonRespVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenpersonrealsituation.vo.ScreenPersonSaveReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenppd.vo.ScreenPpdPageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenppd.vo.ScreenPpdRespVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenppd.vo.ScreenPpdSaveReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screensum.vo.ScreenSumPageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screensum.vo.ScreenSumSaveReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.synchronization.vo.SyncRespVO;
import cn.iocoder.yudao.module.ppd.controller.admin.synchronization.vo.UserLoginInfoVO;
import cn.iocoder.yudao.module.ppd.controller.admin.synchronization.vo.WorkTeamVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenchestradiograph.ScreenChestRadiographDO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screencollect.ScreenCollectDO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenconsumerecord.ScreenConsumeRecordDO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenpersonrealsituation.ScreenPersonDO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenpoint.ScreenPointDO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenppd.ScreenPpdDO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screensum.ScreenSumDO;
import cn.iocoder.yudao.module.ppd.dal.mysql.screenpoint.ScreenPointMapper;
import cn.iocoder.yudao.module.ppd.service.screenconsume.ScreenConsumeService;
import cn.iocoder.yudao.module.ppd.service.screenpersonrealsituation.ScreenPersonService;
import cn.iocoder.yudao.module.ppd.service.synchronization.SynchronizeService;
import cn.iocoder.yudao.module.system.dal.dataobject.dept.DeptDO;
import cn.iocoder.yudao.module.system.service.dept.DeptService;
import cn.iocoder.yudao.module.system.service.user.AdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 同步")
@RestController
@RequestMapping("/tb/synchronize")
@Validated
public class SynchronizeController {

    @Resource
    private SynchronizeService synchronizeService;
    @Resource
    private ScreenPersonService screenPersonService;
    @Resource
    private AdminUserService adminUserService;
    @Resource
    private ScreenConsumeService screenConsumeService;
    @Resource
    private DeptService deptService;
    @Resource
    private ScreenPointMapper screenPointMapper;

//    @GetMapping("/get-tableLists")
//    @Operation(summary = "获取全部业务数据表")
//    @PreAuthorize("@ss.hasPermission('tb:synchronization:query')")
//    public CommonResult<PageResult<TableSchema>> getTableList(PageReqVO pageReqVO) {
//        return success(synchronizeService.getTableList(pageReqVO));
//    }

    @GetMapping("/get-tableData1")
    @Operation(summary = "获取摸底表待筛查人员数据")
//    @PreAuthorize("@ss.hasPermission('tb:synchronization:query')")
    public CommonResult<PageResult<ScreenPersonRespVO>> getTableData1(ScreenPersonPageReqVO pageReqVO) {
        PageResult<ScreenPersonDO> pageResult = synchronizeService.getScreenPersonPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ScreenPersonRespVO.class));
    }

    @PutMapping("/update-tableData1")
    @Operation(summary = "更新摸底表数据")
//    @PreAuthorize("@ss.hasPermission('tb:synchronization:query')")
    public CommonResult<List<SyncRespVO>> updateTableData1(@RequestBody List<ScreenPersonSaveReqVO> list) {
        List<SyncRespVO> syncRespVOS = synchronizeService.updateScreenPerson(list);
        return success(syncRespVOS);
    }


    @GetMapping("/get-tableData2")
    @Operation(summary = "获取采集组数据")
//    @PreAuthorize("@ss.hasPermission('tb:synchronization:query')")
    public CommonResult<PageResult<ScreenCollectRespVO>> getTableData2(ScreenCollectPageReqVO pageReqVO) {
        PageResult<ScreenCollectDO> pageResult = synchronizeService.getCollectPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ScreenCollectRespVO.class));
    }

    @PutMapping("/update-tableData2")
    @Operation(summary = "更新采集表数据")
//    @PreAuthorize("@ss.hasPermission('tb:synchronization:query')")
    public CommonResult<List<SyncRespVO>> updateTableData2(@RequestBody List<ScreenCollectSaveReqVO> list) {
        List<SyncRespVO> syncRespVOS = synchronizeService.updateCollect(list);
        return success(syncRespVOS);
    }

    @GetMapping("/get-tableData3")
    @Operation(summary = "获取ppd组数据")
//    @PreAuthorize("@ss.hasPermission('tb:synchronization:query')")
    public CommonResult<PageResult<ScreenPpdRespVO>> getTableData3(ScreenPpdPageReqVO pageReqVO) {
        PageResult<ScreenPpdDO> pageResult = synchronizeService.getPpdPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ScreenPpdRespVO.class));

    }

    @PutMapping("/update-tableData3")
    @Operation(summary = "更新ppd表数据")
//    @PreAuthorize("@ss.hasPermission('tb:synchronization:query')")
    public CommonResult<List<SyncRespVO> > updateTableData3(@RequestBody List<ScreenPpdSaveReqVO> list) {
        List<SyncRespVO> respVOS = synchronizeService.updatePpd(list);
        return success(respVOS);
    }

    @PostMapping("/upload/consume-record")
    @Operation(summary = "更新试剂消耗")
    public CommonResult<Integer> uploadConsumeRecord(@RequestBody List<ScreenConsumeRecordDO> data){
        Integer batch = screenConsumeService.decreaseScreenConsumeBatch(data);
        return success(batch);
    }
    @GetMapping("/get-tableData4")
    @Operation(summary = "获取dr/ct组数据")
//    @PreAuthorize("@ss.hasPermission('tb:synchronization:query')")
    public CommonResult<PageResult<ScreenChestRadiographRespVO>> getTableData4(ScreenChestRadiographPageReqVO pageReqVO) {
        PageResult<ScreenChestRadiographDO> pageResult = synchronizeService.getChestRadiographPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ScreenChestRadiographRespVO.class));

    }

    @PutMapping("/update-tableData4")
    @Operation(summary = "更新dr/ct表数据")
//    @PreAuthorize("@ss.hasPermission('tb:synchronization:query')")
    public CommonResult<List<SyncRespVO>> updateTableData4(@RequestBody List<ScreenChestRadiographSaveReqVO> list) {
        List<SyncRespVO> respVOS = synchronizeService.updateChestRadiograph(list);
        return success(respVOS);
    }

    @GetMapping("/get-workTeam")
    @Operation(summary = "根据当前用户id 获取所属工作队伍信息")
    @DataPermission(enable = false)
//    @PreAuthorize("@ss.hasPermission('tb:synchronization:query')")
    public CommonResult<List<WorkTeamVO>> getWorkTeam(@RequestParam("screenPointId") Long screenPointId) {
        List<WorkTeamVO> list = synchronizeService.getWorkTeam(screenPointId);
        return success(list);

    }

    @GetMapping("/get-userInfo")
    @Operation(summary = "根据当前用户id 获取信息")
//    @DataPermission(enable = false)
//    @PreAuthorize("@ss.hasPermission('tb:synchronization:query')")
    public CommonResult<UserLoginInfoVO> getPassword(@RequestParam("id") Long id) {
        return success(synchronizeService.getUserInfo(id));

    }


    @GetMapping("/get-sum-data")
    @Operation(summary = "获取汇总表数据")
    public CommonResult<List<ScreenSumDO>> getSumData(ScreenSumPageReqVO pageReqVO) {
//        PageResult<ScreenSumDO> pageResult = synchronizeService.getSumPage(pageReqVO);
//        return success(BeanUtils.toBean(pageResult, ScreenSumRespVO.class));
        List<ScreenSumDO> sumPage = synchronizeService.getSumPage(pageReqVO);
        return success(sumPage);
    }

    @PutMapping("/update-sum-data")
    @Operation(summary = "更新汇总表数据")
    public CommonResult<Boolean> updateSumData(@RequestBody List<ScreenSumSaveReqVO> list) {
        synchronizeService.updateSum(list);
        return success(true);
    }

    @GetMapping("/test/get")
    @Operation(summary = "测试")
    public CommonResult<Object> get(String o){
        ScreenPointDO screenPointDO = screenPointMapper.selectByPointName(o);
        DeptDO dept = deptService.getDept(screenPointDO.getScreenDept());
        return success(dept);
    }
}