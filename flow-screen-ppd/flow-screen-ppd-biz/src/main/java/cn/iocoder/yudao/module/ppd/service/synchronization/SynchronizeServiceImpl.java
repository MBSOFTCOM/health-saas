package cn.iocoder.yudao.module.ppd.service.synchronization;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.ppd.controller.admin.screenchestradiograph.vo.ScreenChestRadiographPageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenchestradiograph.vo.ScreenChestRadiographSaveReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screencollect.vo.ScreenCollectPageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screencollect.vo.ScreenCollectSaveReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenpersonrealsituation.vo.ScreenPersonPageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenpersonrealsituation.vo.ScreenPersonSaveReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenppd.vo.ScreenPpdPageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenppd.vo.ScreenPpdSaveReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screensum.vo.ScreenSumInfo;
import cn.iocoder.yudao.module.ppd.controller.admin.screensum.vo.ScreenSumPageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screensum.vo.ScreenSumSaveReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.synchronization.vo.*;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenchestradiograph.ScreenChestRadiographDO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screencollect.ScreenCollectDO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenpersonrealsituation.ScreenPersonDO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenpoint.ScreenPointDO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenppd.ScreenPpdDO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screensum.ScreenSumDO;
import cn.iocoder.yudao.module.ppd.dal.mysql.screenchestradiograph.ScreenChestRadiographMapper;
import cn.iocoder.yudao.module.ppd.dal.mysql.screencollect.ScreenCollectMapper;
import cn.iocoder.yudao.module.ppd.dal.mysql.screendistrict.ScreenDistrictMapper;
import cn.iocoder.yudao.module.ppd.dal.mysql.screenpersonrealsituation.ScreenPersonMapper;
import cn.iocoder.yudao.module.ppd.dal.mysql.screenpoint.ScreenPointMapper;
import cn.iocoder.yudao.module.ppd.dal.mysql.screenppd.ScreenPpdMapper;
import cn.iocoder.yudao.module.ppd.dal.mysql.screensputumexamination.ScreenSputumExaminationMapper;
import cn.iocoder.yudao.module.ppd.dal.mysql.screensum.ScreenSumMapper;
import cn.iocoder.yudao.module.ppd.dal.mysql.synchronization.SynchronizeMapper;
import cn.iocoder.yudao.module.system.dal.dataobject.dept.DeptDO;
import cn.iocoder.yudao.module.system.service.dept.DeptService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@Validated
public class SynchronizeServiceImpl implements SynchronizeService{

    @Resource
    private SynchronizeMapper synchronizeMapper;
    @Resource
    private ScreenPersonMapper screenPersonMapper;
    @Resource
    private ScreenCollectMapper screenCollectMapper;
    @Resource
    private ScreenPpdMapper screenPpdMapper;
    @Resource
    private ScreenChestRadiographMapper screenChestRadiographMapper;
    @Resource
    private ScreenSputumExaminationMapper screenSputumExaminationMapper;
    @Resource
    private ScreenDistrictMapper screenDistrictMapper;
    @Resource
    private ScreenSumMapper screenSumMapper;
    @Resource
    private DeptService deptService;
    @Resource
    private ScreenPointMapper screenPointMapper;




    @Override
    public PageResult<TableSchema> getTableList(PageReqVO pageReqVO) {
        pageReqVO.setPageNo((pageReqVO.getPageNo()-1)*pageReqVO.getPageSize());
        List<TableSchema> tableList = synchronizeMapper.getTableList(pageReqVO);

        return new PageResult<>(tableList,synchronizeMapper.getTotal(pageReqVO));
    }

    @Override
    public PageResult<ScreenPersonDO> getScreenPersonPage(ScreenPersonPageReqVO pageReqVO) {
        return screenPersonMapper.selectPageForSynchronize(pageReqVO);
    }

    @Override
    public List<SyncRespVO> updateScreenPerson(List<ScreenPersonSaveReqVO> list) {
        List<SyncRespVO> result = new LinkedList<>();
        list.forEach(item->{
            SyncRespVO syncRespVO = new SyncRespVO();
            syncRespVO.setIdNum(item.getIdNum()).setId(item.getId());
            if ("0".equals(item.getScreenId())){
                // 编码规则：省市县乡区划代码9位+排序号5位+年度4位+筛查类型1位
                // 根据乡镇区划、年度以及筛查类型找到个人最大排序号 如果当前乡镇还没有患者，则从1开始排序
                String maxOrderNum = synchronizeMapper.selectMaxOrderNum(item.getYear(), item.getTown(), item.getScreenType());
                String orderNum=null;
                if(maxOrderNum!=null){
                    orderNum = String.format("%05d", Integer.parseInt(maxOrderNum) + 1);
                }else{
                    orderNum = String.format("%05d", 1);
                }
                // 获取当前所在乡镇的区划
//                String code = screenDistrictMapper.selectByName(item.getTown());
                if(item.getTown()!=null && item.getTown().length()==12){
                    String code=item.getTown();// 将code后三位字符去掉，因为乡镇的code为区划code的后三位
                    code=code.substring(0,code.length()-3);
                    item.setScreenId(code+orderNum+item.getYear()+item.getScreenType());
                }
            }

            // 根据 筛查类型screenType,year(工作年度),idNum查询 是否有对应记录的id
            // 有记录 -- 更新
            // 无记录 -- 根据id查记录
            //           无记录 -- 插入
            //           有记录 -- 查询表中id的最大值 ， 更新待插入数据的id值 再插入
            Long id = synchronizeMapper.selectPersonIdByIndex(item.getScreenType(),item.getYear(),item.getIdNum());
            if (id != null ) {
                syncRespVO.setNewId(item.getId());
                syncRespVO.setScreenId(item.getScreenId());
                result.add(syncRespVO);
                synchronizeMapper.updateScreenPerson(item,id);
            }else{
                /*Long result = synchronizeMapper.selectCountById("tb_screen_person", item.getId());
                if (result > 0) {
                    Long maxId = synchronizeMapper.selectMaxId("tb_screen_person");
                    item.setId(maxId+1);
                }
                synchronizeMapper.insertPerson(item);*/
                ScreenPointDO screenPointDO = screenPointMapper.selectByPointName(item.getScreenPoint());
                DeptDO dept = deptService.getDept(screenPointDO.getScreenDept());
                ScreenPersonDO screenPersonDO = BeanUtils.toBean(item, ScreenPersonDO.class);
                screenPersonDO.setId(null);
                screenPersonDO.setDeptId(dept.getId());
                screenPersonMapper.insert(screenPersonDO);
                syncRespVO.setNewId(screenPersonDO.getId());
                syncRespVO.setScreenId(screenPersonDO.getScreenId());
                result.add(syncRespVO);
            }
        });
        return result;
    }

    @Override
    public PageResult<ScreenCollectDO> getCollectPage(ScreenCollectPageReqVO pageReqVO) {
        if(pageReqVO.getPageNo()!=-1){
            pageReqVO.setPageNo((pageReqVO.getPageNo()-1)*pageReqVO.getPageSize());
        }
        PageResult<ScreenCollectDO> pageResult=new PageResult<>(screenCollectMapper.getCollectData(pageReqVO),screenCollectMapper.getCollectCount(pageReqVO));
        return pageResult;
    }
    @Override
    public PageResult<ScreenCollectDO> getCollectPageDiffSource(ScreenCollectPageReqVO pageReqVO) {
        if(pageReqVO.getPageNo()!=-1){
            pageReqVO.setPageNo((pageReqVO.getPageNo()-1)*pageReqVO.getPageSize());
        }
        PageResult<ScreenCollectDO> pageResult=new PageResult<>(screenCollectMapper.selectCollectDataDiffSource(pageReqVO),screenCollectMapper.countCollectDiffSource(pageReqVO));
        return pageResult;
    }

    @Override
    public List<SyncRespVO> updateCollect(List<ScreenCollectSaveReqVO> list) {
        List<SyncRespVO> result = new LinkedList<>();
        list.forEach(item->{
            SyncRespVO syncRespVO = new SyncRespVO();
            syncRespVO.setIdNum(item.getIdNum()).setId(item.getId());
            // 根据筛查编号，筛查次序查询 是否有对应记录的id
            // 有记录 -- 更新
            // 无记录 -- 根据id查记录
            //           无记录 -- 插入
            //           有记录 -- 查询表中id的最大值 ， 更新待插入数据的id值 再插入
            if (item.getStatusFlag()==1){
                ScreenCollectDO collectDO = BeanUtils.toBean(item, ScreenCollectDO.class);
                collectDO.setId(null);
                syncRespVO.setId(item.getId());
                screenCollectMapper.insert(collectDO);
                syncRespVO.setNewId(collectDO.getId());
                result.add(syncRespVO);
            } else if (item.getStatusFlag() == 2) {
                ScreenCollectDO collectDO = BeanUtils.toBean(item, ScreenCollectDO.class);
                Long id = synchronizeMapper.selectCollectIdByIndex(item.getScreenId(), item.getScreenOrder(), item.getPersonId());
                if(id != null){
                    syncRespVO.setNewId(collectDO.getId());
                    result.add(syncRespVO);
                    screenCollectMapper.updateById(collectDO);
                }
            }
        });
        return result;
    }

    @Override
    public PageResult<ScreenPpdDO> getPpdPage(ScreenPpdPageReqVO pageReqVO) {
        if(pageReqVO.getPageNo()!=-1){
            pageReqVO.setPageNo((pageReqVO.getPageNo()-1)*pageReqVO.getPageSize());
        }
        PageResult<ScreenPpdDO> pageResult=new PageResult<>(screenPpdMapper.getPpdData(pageReqVO),screenPpdMapper.getPpdCount(pageReqVO));
        return pageResult;
    }

    @Override
    public List<SyncRespVO>  updatePpd(List<ScreenPpdSaveReqVO> list) {
        List<SyncRespVO> result = new LinkedList<>();
        list.forEach(item->{
            SyncRespVO syncRespVO = new SyncRespVO();
            syncRespVO.setIdNum(item.getIdNum()).setId(item.getId());
            // 根据筛查编号，筛查次序查询 是否有对应记录的id
            // 有记录 -- 更新
            // 无记录 -- 根据id查记录
            //           无记录 -- 插入
            //           有记录 -- 查询表中id的最大值 ， 更新待插入数据的id值 再插入
            if (item.getStatusFlag()==1){
                ScreenPpdDO screenPpdDO = BeanUtils.toBean(item, ScreenPpdDO.class);
                screenPpdDO.setId(null);
                syncRespVO.setId(item.getId());
                screenPpdMapper.insert(screenPpdDO);
                syncRespVO.setNewId(screenPpdDO.getId());
                result.add(syncRespVO);
            } else if (item.getStatusFlag() == 2) {
                ScreenPpdDO screenPpdDO = BeanUtils.toBean(item, ScreenPpdDO.class);
                Long id = synchronizeMapper.selectPpdIdByIndex(item.getScreenId(), item.getScreenOrder(), item.getPersonId());
                if(id != null){
                    syncRespVO.setNewId(screenPpdDO.getId());
                    result.add(syncRespVO);
                    screenPpdMapper.updateById(screenPpdDO);
                }
            }
        });
        return result;
    }

    @Override
    public PageResult<ScreenChestRadiographDO> getChestRadiographPage(ScreenChestRadiographPageReqVO pageReqVO) {
        if(pageReqVO.getPageNo()!=-1){
            pageReqVO.setPageNo((pageReqVO.getPageNo()-1)*pageReqVO.getPageSize());
        }
        PageResult<ScreenChestRadiographDO> pageResult=new PageResult<>(screenChestRadiographMapper.getCtDrData(pageReqVO),screenChestRadiographMapper.getCtDrCount(pageReqVO));
        return pageResult;
    }

    @Override
    public List<SyncRespVO>  updateChestRadiograph(List<ScreenChestRadiographSaveReqVO> list) {
        List<SyncRespVO> result = new LinkedList<>();
        list.forEach(item->{
            SyncRespVO syncRespVO = new SyncRespVO();
            syncRespVO.setIdNum(item.getIdNum()).setId(item.getId());
            // 根据筛查编号，筛查次序查询 是否有对应记录的id
            // 有记录 -- 更新
            // 无记录 -- 根据id查记录
            //           无记录 -- 插入
            //           有记录 -- 查询表中id的最大值 ， 更新待插入数据的id值 再插入
            if (item.getStatusFlag()==1){
                ScreenChestRadiographDO radiographDO = BeanUtils.toBean(item, ScreenChestRadiographDO.class);
                radiographDO.setId(null);
                syncRespVO.setId(item.getId());
                screenChestRadiographMapper.insert(radiographDO);
                syncRespVO.setNewId(radiographDO.getId());
                result.add(syncRespVO);
            } else if (item.getStatusFlag() == 2) {
                ScreenChestRadiographDO radiographDO = BeanUtils.toBean(item, ScreenChestRadiographDO.class);
                Long id = synchronizeMapper.selectChestRadiographIdByIndex(item.getScreenId(), item.getScreenOrder(), item.getPersonId());
                if(id != null){
                    syncRespVO.setNewId(radiographDO.getId());
                    result.add(syncRespVO);
                    screenChestRadiographMapper.updateById(radiographDO);
                }
            }
        });
        return result;
    }


    private static final Map<String, String> GROUP_NAMES =Map.of(
            "worker","队长",
            "collectWorker","采集组",
            "ppdWorker","PPD组",
            "drctWorker","DR/CT组",
            "sputumWorker","痰检组",
            "electrocardiogramWorker","心电图组",
            "experimentWorker","实验室组",
            "diagnosisWorker","诊断组"
    );
    private WorkTeamVO createWorkTeamVO(UserInfoVO userInfo, String agency, String name, Integer year, String groupName) {
        WorkTeamVO workTeamVO = BeanUtils.toBean(userInfo, WorkTeamVO.class);
        try{
            workTeamVO.setAgency(agency)
                    .setScreenPoint(name)
                    .setGroupName(groupName)
                    .setYear(year);
        }catch (Exception e){
            throw e;
        }
        return workTeamVO;
    }
    public List<WorkTeamVO> mergeGroupNames(List<WorkTeamVO> workTeamList) {
        // 创建一个映射，以 id 作为键，以 WorkTeamVO 对象作为值
        Map<Long, WorkTeamVO> teamMap = new HashMap<>();
        List<WorkTeamVO> workTeamVOList=new ArrayList<>();
        // 遍历列表，将 WorkTeamVO 对象添加到映射中，并更新 groupName 属性
        for (WorkTeamVO vo : workTeamList) {
            teamMap.computeIfAbsent(vo.getId(), k -> BeanUtils.toBean(vo,WorkTeamVO.class));
            String groupName="";
            if(!teamMap.get(vo.getId()).getGroupName().equals(vo.getGroupName())){
                if(vo.getGroupName().isEmpty()){
                    groupName=teamMap.get(vo.getId()).getGroupName();
                }else{
                    groupName=teamMap.get(vo.getId()).getGroupName()+","+vo.getGroupName();
                }
            }
            teamMap.get(vo.getId())
                    .setGroupName(groupName.isEmpty()?vo.getGroupName():groupName);
        }
        // 遍历映射，创建一个新的 WorkTeamVO 对象列表，其中 groupName 属性为合并后的字符串
        workTeamVOList = teamMap.values().stream()
                .map(teamVO -> {
                    // 移除 groupName 中的多余逗号（如果有）
                    String mergedGroupName = teamVO.getGroupName().replaceAll("^,|,$", "");
                    teamVO.setGroupName(mergedGroupName);
                    return teamVO;
                })
                .collect(Collectors.toList());

        return workTeamVOList;
    }

    @Override
    public List<WorkTeamVO> getWorkTeam(Long screenPointId) {

        // 根据筛查点名称查询所在筛查点信息及队员id
        ScreenPointAndMemberVO screenPointInfo = synchronizeMapper.getScreenPointInfoByName(screenPointId);
        if (screenPointInfo == null) {
            return Collections.emptyList();
        }

        List<WorkTeamVO> workTeamList= new ArrayList<>();

        String name = Optional.ofNullable(screenPointInfo.getName()).orElse("");
        String agency = Optional.ofNullable(screenPointInfo.getAgency()).orElse("");
        Integer year = screenPointInfo.getYear();

        if(screenPointInfo.getWorker()!=null && !"0".equals(screenPointInfo.getWorker())){
            // 队长信息
            Long capitalId = Long.parseLong(screenPointInfo.getWorker());
            UserInfoVO capitalInfo = synchronizeMapper.getUserInfo(capitalId);
            workTeamList.add(createWorkTeamVO(capitalInfo, agency, name, year, "队长"));
        }

        String collectWorker = screenPointInfo.getCollectWorker();
        if(collectWorker !=null && !"0".equals(collectWorker)){
            // 采集组
            Arrays.stream(collectWorker.split(", "))
                    .map(Long::parseLong)
                    .map(synchronizeMapper::getUserInfo)
                    .map(userInfo -> createWorkTeamVO(userInfo, agency, name, year, GROUP_NAMES.get("collectWorker")))
                    .forEach(workTeamList::add);
        }
        if(screenPointInfo.getPpdWorker()!=null && !"0".equals(screenPointInfo.getPpdWorker())){
            // PPD组
            Arrays.stream(screenPointInfo.getPpdWorker().split(", "))
                    .map(Long::parseLong)
                    .map(synchronizeMapper::getUserInfo)
                    .map(userInfo -> createWorkTeamVO(userInfo, agency, name, year, GROUP_NAMES.get("ppdWorker")))
                    .forEach(workTeamList::add);
        }
        if(screenPointInfo.getDrctWorker()!=null && !"0".equals(screenPointInfo.getDrctWorker())){
            // DR/CT组
            Arrays.stream(screenPointInfo.getDrctWorker().split(", "))
                    .map(Long::parseLong)
                    .map(synchronizeMapper::getUserInfo)
                    .map(userInfo -> createWorkTeamVO(userInfo, agency, name, year, GROUP_NAMES.get("drctWorker")))
                    .forEach(workTeamList::add);
        }
        if(screenPointInfo.getSputumWorker()!=null && !"0".equals(screenPointInfo.getSputumWorker())){
            // 痰检组
            Arrays.stream(screenPointInfo.getSputumWorker().split(", "))
                    .map(Long::parseLong)
                    .map(synchronizeMapper::getUserInfo)
                    .map(userInfo -> createWorkTeamVO(userInfo, agency, name, year, GROUP_NAMES.get("sputumWorker")))
                    .forEach(workTeamList::add);
        }
        if(screenPointInfo.getExperimentWorker()!=null && !"0".equals(screenPointInfo.getExperimentWorker())){
            // 实验组
            Arrays.stream(screenPointInfo.getExperimentWorker().split(", "))
                    .map(Long::parseLong)
                    .map(synchronizeMapper::getUserInfo)
                    .map(userInfo -> createWorkTeamVO(userInfo, agency, name, year, GROUP_NAMES.get("electrocardiogramWorker")))
                    .forEach(workTeamList::add);
        }
        if(screenPointInfo.getElectrocardiogramWorker()!=null && !"0".equals(screenPointInfo.getElectrocardiogramWorker())){
            // 心电组
            Arrays.stream(screenPointInfo.getElectrocardiogramWorker().split(", "))
                    .map(Long::parseLong)
                    .map(synchronizeMapper::getUserInfo)
                    .map(userInfo -> createWorkTeamVO(userInfo, agency, name, year, GROUP_NAMES.get("experimentWorker")))
                    .forEach(workTeamList::add);
        }
        if(screenPointInfo.getDiagnosisWorker()!=null && !"0".equals(screenPointInfo.getDiagnosisWorker())){
            // 诊断组
            Arrays.stream(screenPointInfo.getDiagnosisWorker().split(", "))
                    .map(Long::parseLong)
                    .map(synchronizeMapper::getUserInfo)
                    .map(userInfo -> createWorkTeamVO(userInfo, agency, name, year, GROUP_NAMES.get("diagnosisWorker")))
                    .forEach(workTeamList::add);
        }
        return mergeGroupNames(workTeamList);
    }

    @Override
    public UserLoginInfoVO getUserInfo(Long id) {
        // 获取当前用户基本信息
        UserInfoVO userInfo = synchronizeMapper.getUserInfo(id);
        UserLoginInfoVO userInfoVO = BeanUtils.toBean(userInfo, UserLoginInfoVO.class);
        // 获取筛查点信息和筛查单位
        List<ScreenPointVO> screenPointList = synchronizeMapper.getScreenPointAndAgencyByUserId(id);

        StringBuilder screenPointBuilder = new StringBuilder();
        StringBuilder screenAgencyBuilder = new StringBuilder();

        // 处理筛查点信息
        List<Map<String, Object>> formattedList = new ArrayList<>();
        for (ScreenPointVO vo : screenPointList) {
            Map<String, Object> formattedObject = new HashMap<>();
            formattedObject.put("screenPointId", vo.getScreenPointId());
            formattedObject.put("name", vo.getName());

            formattedList.add(formattedObject);
        }
        // 处理筛查点信息
        for (int i = 0; i < screenPointList.size(); i++) {
            screenPointBuilder.append(screenPointList.get(i).getName());
            screenAgencyBuilder.append(screenPointList.get(i).getScreenDept());
            if (i < screenPointList.size() - 1) {
                screenPointBuilder.append(',');
                screenAgencyBuilder.append(',');
            }
        }
        // 获取角色信息
        List<String> roleList = synchronizeMapper.getRoleByUserId(id);
        StringBuilder groupNameBuilder = new StringBuilder();
        for(int i=0;i<roleList.size();i++){
            // 查询所在分组
            groupNameBuilder.append(roleList.get(i));
            if (i < roleList.size() - 1) {
                groupNameBuilder.append(',');
            }
        }
        userInfoVO.setScreenPoint(screenPointBuilder.toString());
        userInfoVO.setAgency(screenAgencyBuilder.toString());
        userInfoVO.setGroupName(groupNameBuilder.toString());
        userInfoVO.setScreenPointList(formattedList);
        return userInfoVO;
    }


    @Override
    public List<ScreenSumDO> getSumPage(ScreenSumPageReqVO pageReqVO) {
//        pageReqVO.setPageSize(-1);
        return screenSumMapper.selectSumDataForSynchronize(pageReqVO);
    }

    @Override
    public void updateSum(List<ScreenSumSaveReqVO> list) {
        // 根据年份、筛查类型、筛查编号、personId 查询汇总表是否有对应记录的id
        // 有  --  更新
        // 无  --  查询当前id在汇总表中是否有重复记录
        //            无  --  插入
        //            有  --  查询表中最大的id值，更新当前id值再插入
        Map<String,Integer>  groupMap=Map.of(
                "采集组",1,
                "ppd组",2,
                "胸片组",3,
                "痰检组",4,
                "实验组",5,
                "诊断组",6
        );
        list.forEach(item->{
            ScreenSumInfo info = synchronizeMapper.selectSumIdByIndex(Integer.parseInt(item.getYear()), item.getScreenType(), item.getScreenId(), item.getPersonId());
            if(info!=null){
                // 当前已完成分组的值小于数据库中已完成分组的值时，不更新这个字段值
                if (item.getCurFinish()!=null && !"null".equals(item.getCurFinish())){
                    Integer preValue = groupMap.get(info.getCurFinish());
                    Integer curValue = groupMap.get(item.getCurFinish());
                    if(preValue>curValue){
                        item.setCurFinish(info.getCurFinish());
                    }
                }else{
                    item.setCurFinish(info.getCurFinish());
                }
                synchronizeMapper.updateSum(item,info.getId());

            }else{
                Long result = synchronizeMapper.selectCountById("tb_screen_sum", item.getId());
                if(result>0){
                    Long maxId = synchronizeMapper.selectMaxId("tb_screen_sum");
                    item.setId(maxId+1);
                }
                synchronizeMapper.insertSum(item);
            }
        });

    }

    // 找到该乡镇最大的筛查编号
    public static String constructMaxScreenId(List<String> screenIdList, String code, int year, int screenType) {
        int maxPersonalId = 0; // 初始化最大个人编号为 0
        String maxScreenId = "";
        String frontStr = "";
        String lastStr = "";

        if (screenIdList.size() > 0) {
            // 遍历获取到的 screenIdList 列表
            for (String screenId : screenIdList) {
                // 获取个人编号部分
                String personalIdString = screenId.substring(9, 14);
                frontStr = screenId.substring(0, 9);
                lastStr = screenId.substring(14, 19);
                // 将个人编号部分转换为整数
                int personalId = Integer.parseInt(personalIdString);

                // 更新最大个人编号
                if (personalId > maxPersonalId) {
                    maxPersonalId = personalId;
                }
            }
            maxScreenId = frontStr + String.format("%05d", maxPersonalId) + lastStr;
        } else {
            maxScreenId = code + String.format("%05d", maxPersonalId) + String.valueOf(year) + String.valueOf(screenType);
        }

        return maxScreenId;
    }


    // 生成筛查编号
    public static String makeScreenId(String maxScreenId, String code){
        String personalIdString = maxScreenId.substring(9, 14); // 注意：substring的参数是从0开始的索引，所以第10位对应索引9
        String lastStr = maxScreenId.substring(14, 19);
        // 将个人编号部分转换为整数
        int personalId = Integer.parseInt(personalIdString);
        personalId += 1;

        return code + String.format("%05d", personalId) + lastStr;
    }

    //生成姓氏
    public String getFamilyName() {
        String familyName1 = "赵钱孙李周吴郑王冯陈褚卫蒋沈韩杨朱秦尤许何吕施张孔曹严华金魏陶姜戚谢邹喻水云苏潘葛奚范彭郎鲁韦昌马苗凤花方俞任袁柳鲍史唐费岑薛雷贺倪汤滕殷罗毕郝邬安常乐于时傅卞齐康伍余元卜顾孟平"
                + "黄和穆萧尹姚邵湛汪祁毛禹狄米贝明臧计成戴宋茅庞熊纪舒屈项祝董粱杜阮席季麻强贾路娄危江童颜郭梅盛林刁钟徐邱骆高夏蔡田胡凌霍万柯卢莫房缪干解应宗丁宣邓郁单杭洪包诸左石崔吉"
                + "龚程邢滑裴陆荣翁荀羊甄家封芮储靳邴松井富乌焦巴弓牧隗山谷车侯伊宁仇祖武符刘景詹束龙叶幸司韶黎乔苍双闻莘劳逄姬冉宰桂牛寿通边燕冀尚农温庄晏瞿茹习鱼容向古戈终居衡步都耿满弘国文东殴沃曾关红游盖益桓公晋楚闫";
        String familyName2 = "欧阳太史端木上官司马东方独孤南宫万俟闻人夏侯诸葛尉迟公羊赫连澹台皇甫宗政濮阳公冶太叔申屠公孙慕容仲孙钟离长孙宇文司徒鲜于司空闾丘子车亓官司寇巫马公西颛孙壤驷公良漆雕乐正宰父谷梁拓跋夹谷轩辕令狐段干百里呼延东郭南门羊舌微生公户公玉公仪梁丘公仲公上公门公山公坚左丘公伯西门公祖第五公乘贯丘公皙南荣东里东宫仲长子书子桑即墨达奚褚师吴铭";
        String str = "";
        int randNum = new Random().nextInt(2) + 1;
        int strLen = randNum == 1 ? familyName1.length() : familyName2.length();
        int index = new Random().nextInt(strLen);
        if (randNum == 1) {
            str = String.valueOf(familyName1.charAt(index));
        } else {
            str = (index & 1) == 0 ? familyName2.substring(index, index + 2) :
                    familyName2.substring(index - 1, index + 1);
        }
        return str;
    }

    /**
     * 功能：传入性别参数，依据性别产生名字
     *
     * @return
     */
    public String getName() {
        String girlName = "秀娟英华慧巧美娜静淑惠珠翠雅芝玉萍红娥玲芬芳燕彩春菊兰凤洁梅琳素云莲真环雪荣爱妹霞香月莺媛艳瑞凡佳嘉琼勤珍贞莉桂娣叶璧璐娅琦晶妍茜秋珊莎锦黛青倩婷姣婉娴瑾颖露瑶怡婵雁蓓纨仪荷丹蓉眉君琴蕊薇菁梦岚苑婕馨瑗琰韵融园艺咏卿聪澜纯毓悦昭冰爽琬茗羽希宁欣飘育滢馥筠柔竹霭凝晓欢霄枫芸菲寒伊亚宜可姬舒影荔枝思丽";
        String boyName = "伟刚勇毅俊峰强军平保东文辉力明永健世广志义兴良海山仁波宁贵福生龙元全国胜学祥才发武新利清飞彬富顺信子杰涛昌成康星光天达安岩中茂进林有坚和彪博诚先敬震振壮会思群豪心邦承乐绍功松善厚庆磊民友裕河哲江超浩亮政谦亨奇固之轮翰朗伯宏言若鸣朋斌梁栋维启克伦翔旭鹏泽晨辰士以建家致树炎德行时泰盛雄琛钧冠策腾楠榕风航弘";

        Random random = new Random();
        String name;
        if (random.nextBoolean()) { // 随机选择男孩名字或女孩名字
            int index = random.nextInt(girlName.length());
            name = girlName.substring(index, index + 1); // 生成长度为1的随机名字
        } else {
            int index = random.nextInt(boyName.length());
            name = boyName.substring(index, index + 1); // 生成长度为1的随机名字
        }

        return name;
    }

    /**
     * 功能：随机产生18-44的整数
     *
     * @return
     */
    public int getAge() {
        return new Random().nextInt(27) + 18;
    }

    /**
     * 功能：随机产生日期
     *
     * @return
     */
    public String generateRandomDate(String startDate, String endDate,String dateFormatter) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormatter);
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);

        long startEpochDay = start.toEpochDay();
        long endEpochDay = end.toEpochDay();

        long randomDay = startEpochDay + (long) (Math.random() * (endEpochDay - startEpochDay));
        return LocalDate.ofEpochDay(randomDay).format(formatter);
    }

    /**
     * 功能：随机产生身份证号码 513333
     *
     * @return
     */
    public String getIdNum() {

        String date = generateRandomDate("19500101","20101231","yyyyMMdd");

        int a = new Random().nextInt(9000)+1000;

        String idNum = "513333"+date+ a;

        return idNum;
    }

    public String generatePhoneNumber() {
        Random random = new Random();
        StringBuilder phoneNumber = new StringBuilder();

        // 第一位必须是1
        phoneNumber.append(1);

        // 第二位数字范围为3-9
        phoneNumber.append(random.nextInt(7) + 3);

        // 生成后9位数字
        for (int i = 0; i < 9; i++) {
            phoneNumber.append(random.nextInt(10));
        }

        return phoneNumber.toString();
    }

    /**
     * 功能：生成随机时间 的时间戳
     *
     * @return
     */
    public String generateRandomDateWithTime(String startDate, String endDate, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);
            long random = ThreadLocalRandom.current().nextLong(start.getTime(), end.getTime());
            Date resultDate = new Date(random);
            return new SimpleDateFormat("yyyyMMddHHmmss").format(resultDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    public LocalDateTime getTime() {
        String date = generateRandomDateWithTime("20240401", "20240430", "yyyyMMdd");
        LocalDateTime time = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return time;
    }

    /**
     * 功能：生成随机 第一人群分类
     *
     * @return
     */
    public Integer getFirstType() {
        Integer type[] = {1,2};
        return type[new Random().nextInt(2)];
    }

    /**
     * 功能：生成随机 多人群分类
     *
     * @return
     */
    public Integer getMoreType() {
        Integer type[] = {1,2,4,8,16,32,64};
        return type[new Random().nextInt(7)];
    }
}
