package cn.iocoder.yudao.module.ppd.service.screenpoint;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.ppd.controller.admin.screenpersonrealsituation.vo.ScreenPersonImportRespVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenpoint.vo.*;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenpersonrealsituation.ScreenPersonDO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenpoint.ScreenPointDO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.userscreenpoint.UserScreenPointDO;
import cn.iocoder.yudao.module.ppd.dal.mysql.screenpersonrealsituation.ScreenPersonMapper;
import cn.iocoder.yudao.module.ppd.dal.mysql.screenpoint.ScreenPointMapper;
import cn.iocoder.yudao.module.ppd.dal.mysql.userscreenpoint.UserScreenPointMapper;
import cn.iocoder.yudao.module.system.controller.admin.dept.vo.dept.DeptSaveReqVO;
import cn.iocoder.yudao.module.system.controller.admin.user.vo.user.UserRespVO;
import cn.iocoder.yudao.module.system.dal.dataobject.dept.DeptDO;
import cn.iocoder.yudao.module.system.dal.dataobject.permission.UserRoleDO;
import cn.iocoder.yudao.module.system.dal.dataobject.user.AdminUserDO;
import cn.iocoder.yudao.module.system.dal.mysql.dept.DeptMapper;
import cn.iocoder.yudao.module.system.dal.mysql.permission.RoleMapper;
import cn.iocoder.yudao.module.system.dal.mysql.permission.UserRoleMapper;
import cn.iocoder.yudao.module.system.dal.mysql.user.AdminUserMapper;
import cn.iocoder.yudao.module.system.dal.redis.RedisKeyConstants;
import cn.iocoder.yudao.module.system.service.dept.DeptService;
import cn.iocoder.yudao.module.system.service.permission.PermissionService;
import cn.iocoder.yudao.module.system.service.permission.RoleService;
import cn.iocoder.yudao.module.system.service.user.AdminUserService;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertSet;
import static cn.iocoder.yudao.module.cd.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.*;

/**
 * 筛查点 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ScreenPointServiceImpl implements ScreenPointService {

    @Resource
    private ScreenPointMapper screenPointMapper;
    @Resource
    private ScreenPersonMapper screenPersonMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private UserRoleMapper userRoleMapper;
    @Resource
    private AdminUserMapper userMapper;
    @Resource
    private PermissionService permissionService;
    @Resource
    private UserScreenPointMapper userScreenPointMapper;
    @Resource
    private RoleService roleService;
    @Resource
    private DeptService deptService;
    @Resource
    private DeptMapper deptMapper;
    @Resource
    private AdminUserService userService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createScreenPoint(ScreenPointSaveReqVO createReqVO) {
        // 插入
        ScreenPointDO screenPoint = BeanUtils.toBean(createReqVO, ScreenPointDO.class);


        Long screenId2 = screenPointMapper.getIdByName2(screenPoint.getName());

        if (ObjectUtil.isNotNull(screenId2)){
            throw exception(SCREEN_POINT_EXISTS2);
        }

        screenPointMapper.insert(screenPoint);
        // 筛查点id
        Long screenPointId = screenPoint.getId();

        if (StrUtil.isNotEmpty(screenPoint.getWorker())){
            // 队长id
            Long capId = Long.valueOf(screenPoint.getWorker());

            // 拿到队长角色的id
            Long roleId = roleMapper.getIdByCode("capital");
            // 分配新的队长
            UserRoleDO addUserRoleDO = new UserRoleDO();
            addUserRoleDO.setUserId(capId).setRoleId(roleId);
            // 查看 用户角色 表中是否已经分配 该角色
            Long id = userRoleMapper.selcetIsDistribute(capId, roleId);

            if (id == null) {
                // 在 用户角色 表中插入数据
                userRoleMapper.insert(addUserRoleDO);
            }
            // 插入用户筛查点表中
            UserScreenPointDO newUserScreenPoint = new UserScreenPointDO()
                    .setUserId(capId)
                    .setRoleId(roleId)
                    .setScreenPointId(screenPointId)
                    .setUserRoleId(id != null ? id : addUserRoleDO.getId());
            userScreenPointMapper.insert(newUserScreenPoint);
        }
        // 返回
        return screenPoint.getId();
    }

    @Override
    public void updateScreenPoint(ScreenPointSaveReqVO updateReqVO) {
        // 校验存在
        validateScreenPointExists(updateReqVO.getId());
        // 更新
        ScreenPointDO updateObj = BeanUtils.toBean(updateReqVO, ScreenPointDO.class);
        screenPointMapper.updateById(updateObj);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        List<ScreenPointDO> pointDOS = screenPointMapper.selectWorkerIsSomeOne(userId.toString());
        if (pointDOS!=null && pointDOS.size()>0){
            throw exception(WORKER_IS_USABLE);
        }
        List<ScreenPointDO> screenPointDOS = screenPointMapper.selectContainSomeOne(userId.toString());
        List<ScreenPointDO> allWorkers = new ArrayList<>();

        // 遍历每个 ScreenPointDO 对象
        for (ScreenPointDO screenPoint : screenPointDOS) {
            // 将非null的collectWork添加到allWorkers中
            if (screenPoint.getCollectWorker() != null) {
                // 将字符串转换为数组
                String[] array = screenPoint.getCollectWorker().split(",\\s*"); // 使用正则表达式去掉空格
                List<String> collectWorker = new ArrayList<>(Arrays.asList(array));
                collectWorker.removeIf(item-> item.equals(userId.toString()));
                // 将剩余元素转换回字符串
                String result = String.join(", ", collectWorker);
                screenPoint.setCollectWorker(result.length()>0? result : "0");
            }
            // 将非null的ppdWorker添加到allWorkers中
            if (screenPoint.getPpdWorker() != null) {
                // 将字符串转换为数组
                String[] array = screenPoint.getPpdWorker().split(",\\s*"); // 使用正则表达式去掉空格
                List<String> ppdWorker = new ArrayList<>(Arrays.asList(array));
                ppdWorker.removeIf(item-> item.equals(userId.toString()));
                // 将剩余元素转换回字符串
                String result = String.join(", ", ppdWorker);
                screenPoint.setPpdWorker(result.length()>0? result : "0");
            }
            // 将非null的drctWorker添加到allWorkers中
            if (screenPoint.getDrctWorker() != null) {
                // 将字符串转换为数组
                String[] array = screenPoint.getDrctWorker().split(",\\s*"); // 使用正则表达式去掉空格
                List<String> drctWorker = new ArrayList<>(Arrays.asList(array));
                drctWorker.removeIf(item-> item.equals(userId.toString()));
                // 将剩余元素转换回字符串
                String result = String.join(", ", drctWorker);
                screenPoint.setDrctWorker(result.length()>0? result : "0");
            }
            if (screenPoint.getCollectWorker() != null || screenPoint.getPpdWorker() != null || screenPoint.getDrctWorker() != null){
                allWorkers.add(screenPoint);
            }
        }
        screenPointMapper.updateBatch(allWorkers);
        userService.deleteUser(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteScreenPoint(Long id) {
        // 校验存在
        validateScreenPointExists(id);
        ScreenPointDO screenPointDO = screenPointMapper.selectById(id);
        if (StringUtils.isNotEmpty(screenPointDO.getWorker()) && ObjectUtil.isNotNull(screenPointDO.getWorker())){
            Long capId = Long.valueOf(screenPointDO.getWorker());
            // 拿到队长角色的id并删除该队长的角色
            handleRoleAndUsers("capital", capId, id);
        }

        if (StringUtils.isNotEmpty(screenPointDO.getCollectWorker()) && ObjectUtil.isNotNull(screenPointDO.getCollectWorker())){
            // 拿到采集组角色id并处理相关用户
            handleRoleAndUsers("collect_group", screenPointDO.getCollectWorker(), id);
        }

        if (StringUtils.isNotEmpty(screenPointDO.getPpdWorker()) && ObjectUtil.isNotNull(screenPointDO.getPpdWorker())){
            // 拿到 ppd组角色id并处理相关用户
            handleRoleAndUsers("ppd_group", screenPointDO.getPpdWorker(), id);
        }

        if (StringUtils.isNotEmpty(screenPointDO.getDrctWorker()) && ObjectUtil.isNotNull(screenPointDO.getDrctWorker())){
            // 拿到 drct组角色id并处理相关用户
            handleRoleAndUsers("dr/ct_group", screenPointDO.getDrctWorker(), id);
        }

        if (StringUtils.isNotEmpty(screenPointDO.getSputumWorker()) && ObjectUtil.isNotNull(screenPointDO.getSputumWorker())){
            // 拿到 痰检组角色id并处理相关用户
            handleRoleAndUsers("sputum_group", screenPointDO.getSputumWorker(), id);
        }

        if (StringUtils.isNotEmpty(screenPointDO.getExperimentWorker()) && ObjectUtil.isNotNull(screenPointDO.getExperimentWorker())){
            // 拿到 实验组角色id并处理相关用户
            handleRoleAndUsers("experiment_group", screenPointDO.getExperimentWorker(), id);
        }

        if (StringUtils.isNotEmpty(screenPointDO.getElectrocardiogramWorker()) && ObjectUtil.isNotNull(screenPointDO.getElectrocardiogramWorker())){
            // 拿到 心电图组角色id并处理相关用户
            handleRoleAndUsers("electrocardiogram_group", screenPointDO.getElectrocardiogramWorker(), id);
        }

        if (StringUtils.isNotEmpty(screenPointDO.getDiagnosisWorker()) && ObjectUtil.isNotNull(screenPointDO.getDiagnosisWorker())){
            // 拿到 诊断组角色id并处理相关用户
            handleRoleAndUsers("diagnos_group", screenPointDO.getDiagnosisWorker(), id);
        }

        // 删除筛查点
        screenPointMapper.deleteById(id);
        // 删除用户筛查点
        screenPointMapper.deleteByScreenPoingId(id);
    }

    private void handleRoleAndUsers(String roleCode, Object usersOrUserId, Long screenPointId) {
        Long roleId = roleMapper.getIdByCode(roleCode);
        List<Long> userList = new ArrayList<>();

        if (usersOrUserId instanceof String) {
            userList = parseUserIds((String) usersOrUserId);
        } else if (usersOrUserId instanceof Long) {
            userList.add((Long) usersOrUserId);
        }

        for (Long userId : userList) {
            List<UserScreenPointDO> userScreenPointDOList = userScreenPointMapper.selectByUserId(userId);
            boolean flag = true;
            for (UserScreenPointDO obj : userScreenPointDOList) {
                if (obj.getRoleId().equals(roleId) && obj.getUserId().equals(userId)
                        && !obj.getScreenPointId().equals(screenPointId)) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                userRoleMapper.deleteCap(userId, roleId);
            }
        }
    }

    private List<Long> parseUserIds(String users) {
        return Arrays.stream(users.split(","))
                // 去除空格
                .map(String::trim)
                // 转换为Long型
                .map(Long::parseLong)
                // 收集到List中
                .collect(Collectors.toList());
    }

    private void validateScreenPointExists(Long id) {
        if (screenPointMapper.selectById(id) == null) {
            throw exception(SCREEN_POINT_NOT_EXISTS);
        }
    }

    @Override
    public ScreenPointDO getScreenPoint(Long id) {
        return screenPointMapper.selectById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<ScreenPointDO> getScreenPointPage(ScreenPointPageReqVO pageReqVO) {
        // 查询符合条件的初始分页结果
        PageResult<ScreenPointDO> screenPointDOPageResult = screenPointMapper.selectPage(pageReqVO);
        // 获取当前登录用户的角色列表
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        List<String> roleCode = roleService.getRoleCode(loginUserId);
        // 解决 角色 不同，进行数据分割
        resolveRole(roleCode, loginUserId, screenPointDOPageResult);

        return screenPointDOPageResult;
    }

    @Override
    public PageResult<ScreenPointDO> getScreenPointPage2(ScreenPointPageReqVO pageReqVO) {
        PageResult<ScreenPointDO> pageResult = screenPointMapper.selectPage(pageReqVO);
        try {
            // 获取当前登录用户的角色列表
            Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
            List<String> roleCode = roleService.getRoleCode(loginUserId);
            // 解决 角色 不同，进行数据分割
            resolveRole(roleCode, loginUserId, pageResult);

            List<ScreenPointDO> workerList = pageResult.getList();

            for (ScreenPointDO obj : workerList) {
                // 处理 worker 字段
                String worker = obj.getWorker();
                Long capital = Long.valueOf(worker);
                AdminUserDO roleDO = userMapper.selectById(capital);
                if (roleDO != null) {
                    obj.setWorker(roleDO.getNickname());
                }

                // 处理 collectWorker 字段
                String collectWorkerProcessed = processWorkerField(obj.getCollectWorker());
                obj.setCollectWorker(collectWorkerProcessed);

                // 处理 PPDWorker 字段
                String ppdWorkerProcessed = processWorkerField(obj.getPpdWorker());
                obj.setPpdWorker(ppdWorkerProcessed);

                // 处理 drctWorker 字段
                String drctWorkerProcessed = processWorkerField(obj.getDrctWorker());
                obj.setDrctWorker(drctWorkerProcessed);

                // 处理 sputumWorker 字段
                String sputumWorkerProcessed = processWorkerField(obj.getSputumWorker());
                obj.setSputumWorker(sputumWorkerProcessed);

                // 处理 experimentWorker 字段
                String experimentWorkerProcessed = processWorkerField(obj.getExperimentWorker());
                obj.setExperimentWorker(experimentWorkerProcessed);

                // 处理 electrocardiogramWorker 字段
                String electrocardiogramWorkerProcessed = processWorkerField(obj.getElectrocardiogramWorker());
                obj.setElectrocardiogramWorker(electrocardiogramWorkerProcessed);

                // 处理 diagnosisWorker 字段
                String diagnosisWorkerProcessed = processWorkerField(obj.getDiagnosisWorker());
                obj.setDiagnosisWorker(diagnosisWorkerProcessed);
            }

            pageResult.setList(workerList);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return pageResult;
    }


    @Override
    public List<String> getScreenPointList() {
        Long deptId = deptService.getMyDept(SecurityFrameworkUtils.getLoginUserId());
        // 获取所有子部门
        List<DeptDO> deptList = deptService.getChildDeptList(deptId);
        // 以及当前部门
        deptList.add(deptService.getDept(deptId));

        // 提取部门名称并存储到列表中
        List<String> deptNameList = deptList.stream()
                .map(DeptDO::getName).toList();

        List<String> list = screenPointMapper.getScreenPointList(deptNameList);

        return list;
    }

    @Override
    public Boolean screenUpdatePoint(ScreenUpdateVO screenUpdateVO) {
        List<Long> list = screenUpdateVO.getIds();
        String name = screenUpdateVO.getName();
        // 创建存储 ScreenPointDO 对象的列表
        List<ScreenPersonDO> screenPointList = new ArrayList<>();
        for (Long obj : list) {
            ScreenPersonDO screenPersonDO = new ScreenPersonDO();
            screenPersonDO.setId(obj).setScreenPoint(name);
            screenPointList.add(screenPersonDO);
        }

        // 调用 mapper 的 updateBatch 方法
        return screenPersonMapper.updateBatch(screenPointList);
    }

    @Override
    public ScreenPersonImportRespVO importScreenPoint(List<ScreenPointImportVO> list) {

        // 初始化变量
        List<ScreenPointDO> batchInsert = new ArrayList<>();
        List<ScreenPointDO> batchUpdate = new ArrayList<>();
        List<String> createSpecification = new ArrayList<>();
        Map<Integer, String> failureSpecification = new HashMap<>();
        ScreenPersonImportRespVO screenPersonImportRespVO = ScreenPersonImportRespVO.builder().build();

        // 数据去重
        List<ScreenPointImportVO> distinctList = list.stream().distinct().collect(Collectors.toList());

        if (!list.isEmpty()) {
            list.remove(list.size() - 1);
        }

        for (ScreenPointImportVO obj : distinctList) {
            Long id = screenPointMapper.getIdByName(obj.getName(), obj.getYear(), obj.getScreenDept());
            if (id == null) {
                batchInsert.add(BeanUtils.toBean(obj, ScreenPointDO.class));
                createSpecification.add("");
            } else {
                batchUpdate.add(BeanUtils.toBean(obj, ScreenPointDO.class));
                failureSpecification.put(failureSpecification.size(), "该筛查点已存在！");
            }
        }

        // 批量插入和更新
        if (!batchInsert.isEmpty()) {
            screenPointMapper.insertBatch(batchInsert);
        }
        if (!batchUpdate.isEmpty()) {
            screenPointMapper.updateBatch(batchUpdate);
        }

        return screenPersonImportRespVO
                .setCreateSpecification(createSpecification)
                .setFailureSpecification(failureSpecification);
    }

    @Override
    public List<ScreenPointImportVO> createSampleData() {
        return List.of(
                ScreenPointImportVO.builder().name("凤凰街道").screenDept("下拉选").year(LocalDate.now().getYear()).build()
        );
    }

    @Override
    public Long getUserLoginAgencyId() {
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        return screenPointMapper.selectByIdTaskDeptId(loginUserId);
    }

    @Override
    public List<UserVO> getUserList(Long deptId) {

        List<DeptVO> list = getChildDeptList(deptId);

        List<Long> deptIds = new ArrayList<>();

        for (DeptVO obj : list) {
            deptIds.add(obj.getId());
        }
        deptIds.add(deptId);

        List<UserVO> userList = screenPointMapper.getUserList(deptIds);

        return userList;
    }

    @Override
    public List<DeptVO> getChildDeptList(Long deptId) {
        List<DeptVO> children = new LinkedList<>();
        // 遍历每一层
        Collection<Long> parentIds = Collections.singleton(deptId);
        for (int i = 0; i < Short.MAX_VALUE; i++) { // 使用 Short.MAX_VALUE 避免 bug 场景下，存在死循环
            // 查询当前层，所有的子部门
            List<DeptVO> depts = screenPointMapper.selectListByParentId(parentIds);
            // 1. 如果没有子部门，则结束遍历
            if (CollUtil.isEmpty(depts)) {
                break;
            }
            // 2. 如果有子部门，继续遍历
            children.addAll(depts);
            parentIds = convertSet(depts, DeptVO::getId);
        }
        return children;
    }

    @Override
    public List<UserVO> getAllUserList() {
        return screenPointMapper.getAllUserList();
    }

    @Override
    public List<DeptVO> getDeptList() {
        return screenPointMapper.getDeptList();
    }

    @Override
    public void setWorker(Long userId, Long screenPointId) {
        screenPointMapper.setWorker(String.valueOf(userId), screenPointId);
    }

    /*@Override
    public Long getIsDistribute(Long userId) {
        return screenPointMapper.getIsDistribute(userId);
    }*/

    @Override
    public Boolean getDistributeYear(Long userId, Integer year) {
        Long isDistribute = screenPointMapper.getDistributeYear(String.valueOf(userId), year);
        return isDistribute != null;
    }

    @Override
    public List<UserRespVO> getCollectList(List<Long> collectList) {
        return screenPointMapper.getCollectList(collectList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateScreenPoint(Long screenPointId, Long userId, Long roleId) {
        Long userRoleId = userRoleMapper.selectByuserIdAndRoleId(userId, roleId);
        UserScreenPointDO userScreenPointDO = new UserScreenPointDO()
                .setScreenPointId(screenPointId)
                .setUserId(userId)
                .setRoleId(roleId)
                .setUserRoleId(userRoleId);
        userScreenPointMapper.insert(userScreenPointDO);
    }

    @Override
    public void setCollect(String collectUserIdStr, Long screenPointId, Integer type) {
        try {
            switch (type) {
                case 8:
                    screenPointMapper.setWorker2(collectUserIdStr, screenPointId);
                    break;
                case 2:
                    screenPointMapper.setPPDWorker(collectUserIdStr, screenPointId);
                    break;
                case 3:
                    screenPointMapper.setDRCTWorker(collectUserIdStr, screenPointId);
                    break;
                case 4:
                    screenPointMapper.setSputumWorker(collectUserIdStr, screenPointId);
                    break;
                case 5:
                    screenPointMapper.setExperimentWorker(collectUserIdStr, screenPointId);
                    break;
                case 6:
                    screenPointMapper.setElectrocardiogramWorker(collectUserIdStr, screenPointId);
                    break;
                case 7:
                    screenPointMapper.setDiagnosisWorker(collectUserIdStr, screenPointId);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid type: " + type);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteScreenPointCollect(String collectWorker, Long screenPointId, Long deletedData, Integer type) {
        Long roleId = null;

        try {
            switch (type) {
                case 8:
                    screenPointMapper.setCollectWorkder(collectWorker, screenPointId);
                    roleId = permissionService.getCollectRoleId();
                    break;
                case 2:
                    screenPointMapper.setPPDWorker(collectWorker, screenPointId);
                    roleId = permissionService.getPPDRoleId();
                    break;
                case 3:
                    screenPointMapper.setDRCTWorker(collectWorker, screenPointId);
                    roleId = permissionService.getDrctRoleId();
                    break;
                case 4:
                    screenPointMapper.setSputumWorker(collectWorker, screenPointId);
                    roleId = permissionService.getSputumRoleId();
                    break;
                case 5:
                    screenPointMapper.setExperimentWorker(collectWorker, screenPointId);
                    roleId = permissionService.getExperimentRoleId();
                    break;
                case 6:
                    screenPointMapper.setElectrocardiogramWorker(collectWorker, screenPointId);
                    roleId = permissionService.getElectrocardiogramRoleId();
                    break;
                case 7:
                    screenPointMapper.setDiagnosisWorker(collectWorker, screenPointId);
                    roleId = permissionService.getDiagnosisRoleId();
                    break;
                default:
                    break;
            }

            if (roleId != null) {
                boolean flag = true;
                List<UserScreenPointDO> userScreenPointDOList = userScreenPointMapper.selectByUserId(deletedData);
                for (UserScreenPointDO obj : userScreenPointDOList) {
                    if (!obj.getScreenPointId().equals(screenPointId) && obj.getUserId().equals(deletedData) && obj.getRoleId().equals(roleId)) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    userRoleMapper.setDeletedData(deletedData, roleId);
                }
            }

            userScreenPointMapper.deleteByUserIdAndRoleIdAndScreenPointId(deletedData, roleId, screenPointId);
        } catch (Exception e) {
            // 处理异常
            throw new IllegalArgumentException("Invalid type: " + type);
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setCap(Long newCapId, Long oldCapId, Long screenPointId) {
        // 获取队长角色的ID
        Long roleId = roleMapper.getIdByCode("capital");

        // 处理新队长
        handleNewCaptain(newCapId, roleId, screenPointId);

        if (ObjectUtil.isNotNull(oldCapId)){
            // 处理旧队长
            handleOldCaptain(oldCapId, roleId, screenPointId);
        }
    }

    // 处理新队长
    private void handleNewCaptain(Long newCapId, Long roleId, Long screenPointId) {
        /*// 查询新队长的角色列表
        List<UserRoleDO> userRoleNewCapList = userRoleMapper.selectListByUserId(newCapId);
        // 判断新队长是否已经拥有队长角色
        boolean isNewCapAlreadyCaptain = false;
        Long newUserRoleId = null;
        for (UserRoleDO userRole : userRoleNewCapList) {
            if (userRole.getRoleId().equals(roleId)) {
                isNewCapAlreadyCaptain = true;
                newUserRoleId = userRole.getId();
                break;
            }
        }
        // 如果新队长原来没有队长角色，则分配角色并添加用户筛查点
        if (!isNewCapAlreadyCaptain) {
            UserRoleDO newUserRoleDO = new UserRoleDO().setUserId(newCapId).setRoleId(roleId);
            userRoleMapper.insert(newUserRoleDO);
            newUserRoleId = newUserRoleDO.getId();
        }*/
        Long newUserRoleId = userRoleMapper.selectByuserIdAndRoleId(newCapId, roleId);

        // 添加用户筛查点
        UserScreenPointDO newUserScreenPoint = new UserScreenPointDO()
                .setUserId(newCapId)
                .setRoleId(roleId)
                .setScreenPointId(screenPointId)
                .setUserRoleId(newUserRoleId);
        userScreenPointMapper.insert(newUserScreenPoint);
    }

    // 处理 旧队长
    private void handleOldCaptain(Long oldCapId, Long roleId, Long screenPointId) {
        // 查询旧队长的用户筛查点列表
        List<UserScreenPointDO> userScreenPointOldCapList = userScreenPointMapper.selectByUserId(oldCapId);
        boolean shouldDeleteUserRole = true;
        // 检查是否还有其他关联的用户筛查点
        for (UserScreenPointDO userScreenPoint : userScreenPointOldCapList) {
            if (userScreenPoint.getRoleId().equals(roleId)
                    && !userScreenPoint.getScreenPointId().equals(screenPointId)
                    && userScreenPoint.getUserId().equals(oldCapId)) {
                shouldDeleteUserRole = false;
                break;
            }
        }
        // 删除旧队长的角色（如果条件符合）
        if (shouldDeleteUserRole) {
            userRoleMapper.deleteCap(oldCapId, roleId);
        }
        // 删除用户筛查点
        userScreenPointMapper.deleteByUserIdAndRoleIdAndScreenPointId(oldCapId, roleId, screenPointId);
    }

    // 解决 角色 不同，进行数据分割
    private void resolveRole(List<String> roleCode, Long loginUserId, PageResult<ScreenPointDO> screenPointDOPageResult){
        if (ObjectUtil.isNotNull(roleCode) && ObjectUtil.isNotEmpty(roleCode)) {
            boolean hasManagerRole = false;
            boolean hasCaptainRole = false;
            // 判断是否具有县级、市级或省级管理角色
            for (String role : roleCode) {
                if ("county_manager".equals(role) || "city_manager".equals(role) || "province_manager".equals(role) ||"teacher".equals(role)) {
                    hasManagerRole = true;
                    break;
                }
            }
            if (!hasManagerRole){
                for (String role : roleCode) {
                    if ("capital".equals(role)){
                        hasCaptainRole = true;
                        break;
                    }
                }
            }

            List<ScreenPointDO> filteredList = new ArrayList<>();
            // 如果具有管理角色，则进行部门数据过滤
            if (hasManagerRole) {
                // 获取当前登录用户子部门列表
                List<DeptVO> childDeptList = getChildDeptList(screenPointMapper.selectByIdTaskDeptId(loginUserId));
                Long myDept = deptService.getMyDept(loginUserId);
                DeptDO dept = deptService.getDept(myDept);
                // 把所属部门添加进去
                childDeptList.add(BeanUtils.toBean(dept, DeptVO.class));
                // 遍历初始分页结果，筛选符合部门条件的数据
                if (screenPointDOPageResult.getList().size() > 0){
                    for (ScreenPointDO obj : screenPointDOPageResult.getList()) {
                        for (DeptVO deptVO : childDeptList) {
                            if (obj.getScreenDept().equals(deptVO.getName())) {
                                filteredList.add(obj);
                                // 找到匹配部门后，不再继续比较下一个部门
                                break;
                            }
                        }
                    }
                    // 更新分页结果为符合部门条件的列表
                    screenPointDOPageResult.setList(filteredList);
                }
            }

            // 如果具有队长角色
            if (hasCaptainRole){
                // 遍历初始分页结果
                if (screenPointDOPageResult.getList().size() > 0){
                    for (ScreenPointDO obj : screenPointDOPageResult.getList()) {
                        if (Long.valueOf(obj.getWorker()).equals(loginUserId)){
                            filteredList.add(obj);
                        }
                    }
                    // 更新分页结果为符合部门条件的列表
                    screenPointDOPageResult.setList(filteredList);
                }
            }
        }
    }

    // 将用户id转化成昵称，在筛查点导出时
    private String processWorkerField(String workerField) {
        if (workerField == null || workerField.isEmpty()) {
            return "";
        }

        StringBuilder workerStr = new StringBuilder();
        String[] workerIds = workerField.split(", ");
        for (String workerId : workerIds) {
            Long id = Long.valueOf(workerId);
            AdminUserDO user = userMapper.selectById(id);
            if (user != null) {
                workerStr.append(user.getNickname()).append(", ");
            }
        }

        // 去除末尾的逗号和空格
        if (workerStr.length() > 0) {
            // 减去最后的 ", "
            workerStr.setLength(workerStr.length() - 2);
        }
        return workerStr.toString();
    }


    @Override
    @CacheEvict(cacheNames = RedisKeyConstants.DEPT_CHILDREN_ID_LIST,
            allEntries = true) // allEntries 清空所有缓存，因为操作一个部门，涉及到多个缓存
    @Transactional(rollbackFor = Exception.class)
    public void updateDept(DeptSaveReqVO updateReqVO) {
        if (updateReqVO.getParentId() == null) {
            updateReqVO.setParentId(DeptDO.PARENT_ID_ROOT);
        }
        // 校验自己存在
        validateDeptExists(updateReqVO.getId());
        // 校验父部门的有效性
        validateParentDept(updateReqVO.getId(), updateReqVO.getParentId());
        // 校验部门名的唯一性
        validateDeptNameUnique(updateReqVO.getId(), updateReqVO.getParentId(), updateReqVO.getName());

        // 1.拿到这个部门信息
        DeptDO deptDO = deptMapper.selectById(updateReqVO.getId());
        // 2.拿到 原来的部门名称
        String deptName = deptDO.getName();
        // 3.拿到相关联的筛查点
        List<ScreenPointDO> screenPointList = screenPointMapper.getByDeptName(deptName);
        Collection<ScreenPointDO> updateBath = new ArrayList<>();
        if (screenPointList != null && !screenPointList.isEmpty()){
            for (ScreenPointDO obj : screenPointList) {
                obj.setScreenDept(updateReqVO.getName());
                updateBath.add(obj);
            }
        }

        // 更新部门
        DeptDO updateObj = BeanUtils.toBean(updateReqVO, DeptDO.class);
        deptMapper.updateById(updateObj);

        // 4.更新相关的筛查点
        if (!updateBath.isEmpty()){
            screenPointMapper.updateBatch(updateBath);
        }
    }

    @VisibleForTesting
    void validateDeptExists(Long id) {
        if (id == null) {
            return;
        }
        DeptDO dept = deptMapper.selectById(id);
        if (dept == null) {
            throw exception(DEPT_NOT_FOUND);
        }
    }

    @VisibleForTesting
    void validateParentDept(Long id, Long parentId) {
        if (parentId == null || DeptDO.PARENT_ID_ROOT.equals(parentId)) {
            return;
        }
        // 1. 不能设置自己为父部门
        if (Objects.equals(id, parentId)) {
            throw exception(DEPT_PARENT_ERROR);
        }
        // 2. 父部门不存在
        DeptDO parentDept = deptMapper.selectById(parentId);
        if (parentDept == null) {
            throw exception(DEPT_PARENT_NOT_EXITS);
        }
        // 3. 递归校验父部门，如果父部门是自己的子部门，则报错，避免形成环路
        if (id == null) { // id 为空，说明新增，不需要考虑环路
            return;
        }
        for (int i = 0; i < Short.MAX_VALUE; i++) {
            // 3.1 校验环路
            parentId = parentDept.getParentId();
            if (Objects.equals(id, parentId)) {
                throw exception(DEPT_PARENT_IS_CHILD);
            }
            // 3.2 继续递归下一级父部门
            if (parentId == null || DeptDO.PARENT_ID_ROOT.equals(parentId)) {
                break;
            }
            parentDept = deptMapper.selectById(parentId);
            if (parentDept == null) {
                break;
            }
        }
    }

    @VisibleForTesting
    void validateDeptNameUnique(Long id, Long parentId, String name) {
        DeptDO dept = deptMapper.selectByParentIdAndName(parentId, name);
        if (dept == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的部门
        if (id == null) {
            throw exception(DEPT_NAME_DUPLICATE);
        }
        if (ObjectUtil.notEqual(dept.getId(), id)) {
            throw exception(DEPT_NAME_DUPLICATE);
        }
    }
}