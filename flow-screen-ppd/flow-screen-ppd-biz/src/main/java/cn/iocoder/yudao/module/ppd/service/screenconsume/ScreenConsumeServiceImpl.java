package cn.iocoder.yudao.module.ppd.service.screenconsume;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.ppd.controller.admin.screenconsume.vo.ScreenConsumeImportVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenconsume.vo.ScreenConsumePageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenconsume.vo.ScreenConsumeSaveReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenconsume.vo.ScreenConsumeStatisticsRespVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenreagent.vo.ScreenReagentImportRespVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenconsume.ScreenConsumeDO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenconsumerecord.ScreenConsumeRecordDO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenreagent.ScreenReagentDO;
import cn.iocoder.yudao.module.ppd.dal.mysql.screenconsume.ScreenConsumeMapper;
import cn.iocoder.yudao.module.ppd.dal.mysql.screenconsumerecord.ScreenConsumeRecordMapper;
import cn.iocoder.yudao.module.ppd.dal.mysql.screenreagent.ScreenReagentMapper;
import cn.iocoder.yudao.module.system.service.dept.DeptService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.cd.enums.ErrorCodeConstants.*;

/**
 * 消耗管理 Service 实现类
 *
 * @author 侯卿
 */
@Service
@Validated
public class ScreenConsumeServiceImpl implements ScreenConsumeService {

    @Resource
    private ScreenConsumeMapper screenConsumeMapper;

    @Resource
    private ScreenConsumeRecordMapper screenConsumeRecordMapper;

    @Resource
    private ScreenReagentMapper screenReagentMapper;

    @Resource
    private DeptService deptService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createScreenConsume(ScreenConsumeSaveReqVO createReqVO) {
        // 插入
        ScreenConsumeDO screenConsume = BeanUtils.toBean(createReqVO, ScreenConsumeDO.class);

        Long deptId = deptService.getMyDept(SecurityFrameworkUtils.getLoginUserId());

        Integer countOrder = screenConsumeMapper.countOrderByReagent(createReqVO.getReagentId(), deptId,createReqVO.getConsumeOrder());
        if (countOrder!=null && countOrder>0){
            throw exception(SCREEN_CONSUME_ORDER_IS_EXISTS);
        }

        Integer count =
                screenConsumeMapper.isExist(screenConsume.getReagentId(),
                        screenConsume.getConsumeOrder(), screenConsume.getBathNumber(),
                        screenConsume.getIndate(), screenConsume.getManufactureDate(), deptId);

        if (count > 0) {
            throw exception(SCREEN_CONSUME_IS_EXISTS);
        }

        screenConsume.setDeptId(deptId);
        screenConsumeMapper.insert(screenConsume);
        screenConsumeRecordMapper.insert(
                new ScreenConsumeRecordDO().setConsumeId(screenConsume.getId())
                        .setType(4).setChangeNumber(screenConsume.getInboundNumber()));
        // 返回
        return screenConsume.getId();
    }

    @Override
    public void updateScreenConsume(ScreenConsumeSaveReqVO updateReqVO) {
        // 校验存在
        validateScreenConsumeExists(updateReqVO.getId());
        // 更新
        ScreenConsumeDO updateObj = BeanUtils.toBean(updateReqVO, ScreenConsumeDO.class);

        Long deptId = deptService.getMyDept(SecurityFrameworkUtils.getLoginUserId());

        Integer count =
                screenConsumeMapper.isExist(updateObj.getReagentId(),
                        updateObj.getConsumeOrder(), updateObj.getBathNumber(),
                        updateObj.getIndate(), updateObj.getManufactureDate(), deptId);

        if (count > 1) {
            throw exception(SCREEN_CONSUME_IS_EXISTS);
        }

        screenConsumeMapper.updateById(updateObj);
    }

    @Override
    public void deleteScreenConsume(Long id) {
        // 校验存在
        validateScreenConsumeExists(id);
        // 删除
        screenConsumeMapper.deleteById(id);
    }

    private void validateScreenConsumeExists(Long id) {
        if (screenConsumeMapper.selectById(id) == null) {
            throw exception(SCREEN_CONSUME_NOT_EXISTS);
        }
    }

    @Override
    public ScreenConsumeDO getScreenConsume(Long id) {
        return screenConsumeMapper.selectById(id);
    }

    @Override
    public PageResult<ScreenConsumeDO> getScreenConsumePage(ScreenConsumePageReqVO pageReqVO) {

        if (pageReqVO.getDeptList() == null){
            Long myDeptId = deptService.getMyDept(SecurityFrameworkUtils.getLoginUserId());

            /*List<DeptDO> deptList = new ArrayList<>();
            deptList.add(deptService.getDept(myDeptId));

            // 提取部门ID
            List<Long> deptIds = deptList.stream()
                .map(DeptDO::getId).toList();*/

            pageReqVO.setDeptList(myDeptId);
        }

        return screenConsumeMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ScreenConsumeDO> getUsableScreenConsume(ScreenConsumePageReqVO pageReqVO) {
        pageReqVO.setDeptId(deptService.getMyDept(SecurityFrameworkUtils.getLoginUserId()));
        return screenConsumeMapper.listUsable(pageReqVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean increaseScreenConsume(Long id, Integer number) {

        Integer count = screenConsumeMapper.increaseScreenConsume(id, number);

        int insertCount =
                screenConsumeRecordMapper.insert(
                        new ScreenConsumeRecordDO()
                                .setConsumeId(id)
                                .setChangeNumber(number)
                                .setType(2));

        return count > 0 && insertCount > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean decreaseScreenConsume(Long id, Integer number) {

        ScreenConsumeDO screenConsumeDO = screenConsumeMapper.selectById(id);

        if (screenConsumeDO.getCurrentNumber() - number >= 0) {
            Integer count = screenConsumeMapper.decreaseScreenConsume(id, number);

            int insertCount =
                    screenConsumeRecordMapper.insert(
                            new ScreenConsumeRecordDO()
                                    .setConsumeId(id)
                                    .setChangeNumber(number)
                                    .setType(3));

            return count > 0 && insertCount > 0;
        } else {
            throw exception(SCREEN_CONSUME_CURRENT_NUMBER_IS_NOT_ENOUGH);
        }
    }

    @Override
    public List<ScreenConsumeImportVO> createSampleData() {
        return List.of(
                ScreenConsumeImportVO.builder().reagentName("重组结核杆菌融合蛋白（EC）").consumeOrder(1)
                        .bathNumber("41886-3").inboundNumber(500).indate("180").manufactureDate("2024/8/9")
                        .build()
        );
    }

    @Override
    public ScreenReagentImportRespVO importScreenConsume(List<ScreenConsumeImportVO> list) {

        // 使用 Stream 过滤空对象
        List<ScreenConsumeImportVO> filteredList = list.stream()
                .filter(vo -> !vo.isEmpty(vo)).toList();

        // 数据去重
        List<ScreenConsumeImportVO> distinctImportList = filteredList.stream().distinct().toList();

        // 批量导入列表
        List<ScreenConsumeDO> batchInsert = new ArrayList<>();
        // 导入成功的列表
        List<String> createSpecification = new ArrayList<>();
        //导入失败的列表
        Map<Integer, String> failureSpecification = new HashMap<>();
        // 返回的结果
        ScreenReagentImportRespVO screenReagentImportRespVO =
                ScreenReagentImportRespVO.builder().build();
        // 记录顺序
        Integer count = 1;

        Long myDeptId = deptService.getMyDept(SecurityFrameworkUtils.getLoginUserId());

        if (distinctImportList.isEmpty()) {
            return screenReagentImportRespVO;
        }

        for (ScreenConsumeImportVO obj : distinctImportList) {
            if (ObjectUtil.isNull(obj.getReagentName())) {
                failureSpecification.put(count, "没有选择试剂");
            } else if (ObjectUtil.isNull(obj.getConsumeOrder())) {
                failureSpecification.put(count, "消耗序位为空");
            } else if (ObjectUtil.isNull(obj.getBathNumber())) {
                failureSpecification.put(count, "批次号为空");
            } else if (ObjectUtil.isNull(obj.getInboundNumber())) {
                failureSpecification.put(count, "入库量为空");
            } else if (ObjectUtil.isNull(obj.getManufactureDate())) {
                failureSpecification.put(count, "生产日期为空");
            } else if (ObjectUtil.isNull(obj.getIndate())) {
                failureSpecification.put(count, "有效期为空");
            } else {
                String manufactureDateStr = obj.getManufactureDate();
                // 创建一个 DateTimeFormatter 对象来解析字符串
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/M/d");

                // 解析字符串为 LocalDate 对象
                LocalDate localDate = LocalDate.parse(manufactureDateStr, formatter);

                // 将 LocalDate 转换为 LocalDateTime，时间部分默认为 00:00:00
                LocalDateTime localDateTime = localDate.atStartOfDay();
                Integer isExist =
                        screenConsumeMapper.isExist2(obj.getReagentName(),
                                obj.getConsumeOrder(), obj.getBathNumber(),
                                obj.getIndate(), localDateTime, myDeptId);
                if (isExist > 0) {
                    failureSpecification.put(count, "该消耗管理已存在");
                } else {
                    ScreenReagentDO screenReagentDO =
                            screenReagentMapper.selectByName(obj.getReagentName(), myDeptId);

                    ScreenConsumeDO screenConsumeDO = new ScreenConsumeDO();
                    BeanUtil.copyProperties(obj, screenConsumeDO);
                    screenConsumeDO.setReagentId(screenReagentDO.getId())
                            .setReagentType(screenReagentDO.getType())
                            .setUsable(screenReagentDO.getUsable())
                            .setThreshold(screenReagentDO.getThreshold())
                            .setReagentSpecsNum(screenReagentDO.getReagentSpecsNum())
                            .setCurrentNumber(obj.getInboundNumber())
                            .setManufactureDate(localDateTime)
                            .setDeptId(myDeptId);

                    batchInsert.add(screenConsumeDO);
                    createSpecification.add("");
                }
            }
            count++;
        }

        // 使用 Set 来自动处理唯一性
        Set<ScreenConsumeDO> distinctSet = new HashSet<>(batchInsert);
        Collection<ScreenConsumeRecordDO> consumeRecordDOCollection = new ArrayList<>();

        // 批量导入消耗管理
        for (ScreenConsumeDO screenConsumeDO : distinctSet) {
            screenConsumeMapper.insert(screenConsumeDO);
            ScreenConsumeRecordDO screenConsumeRecordDO = new ScreenConsumeRecordDO()
                    .setConsumeId(screenConsumeDO.getId())
                    .setChangeNumber(screenConsumeDO.getInboundNumber())
                    .setType(4);
            consumeRecordDOCollection.add(screenConsumeRecordDO);
        }

        // 批量插入消耗记录，如果集合不为空
        if (!consumeRecordDOCollection.isEmpty()) {
            screenConsumeRecordMapper.insertBatch(consumeRecordDOCollection);
        }

        return screenReagentImportRespVO.setCreateSpecification(createSpecification)
                .setFailureSpecification(failureSpecification);
    }

    @Override
    public List<ScreenConsumeStatisticsRespVO> getScreenConsumeStatistics(ScreenConsumePageReqVO pageReqVO) {
        // 检查请求参数中的创建时间是否不为空且长度大于1
        if (ObjectUtil.isNotNull(pageReqVO.getCreateTime()) && pageReqVO.getCreateTime().length > 1) {
            // 创建一个列表来存放结果
            List<ScreenConsumeStatisticsRespVO> resultList = new ArrayList<>();

            Long deptId;

            if (pageReqVO.getDeptList() == null){
                // 获取当前用户的部门ID
                deptId = deptService.getMyDept(SecurityFrameworkUtils.getLoginUserId());
            }else {
                deptId = pageReqVO.getDeptList();
            }

            // 获取请求参数中的开始时间和结束时间
            LocalDateTime[] createTime = pageReqVO.getCreateTime();
            LocalDateTime startDateTime = createTime[0];
            LocalDateTime endDateTime = createTime[1];

            // 计算时间区间的天数
            long periodDays = ChronoUnit.DAYS.between(startDateTime, endDateTime);

            // 计算前一个时间区间的开始和结束时间
            LocalDateTime previousStartDateTime = startDateTime.minusDays(periodDays + 1);
            LocalDateTime previousEndDateTime = endDateTime.minusDays(periodDays + 1);

            // 查询当前时间区间内的消耗数据
            List<ScreenConsumeDO> list =
                    screenConsumeMapper.selectByTime(startDateTime, endDateTime, pageReqVO.getReagentName(), deptId);

            System.out.println(list);
            // 查询前一个时间区间内的消耗数据
            List<ScreenConsumeDO> preList =
                    screenConsumeMapper.selectByTime(previousStartDateTime, previousEndDateTime, pageReqVO.getReagentName(), deptId);
            System.out.println(preList);

            // 如果当前时间区间的列表不为空
            if (ObjectUtil.isNotNull(list) && !list.isEmpty()) {
                // 创建一个用于存储前一个时间区间数据的映射
                Map<String, ScreenConsumeDO> preListMap = new HashMap<>();
                // 如果前一个时间区间的列表不为空，将数据存储到映射中
                if (ObjectUtil.isNotNull(preList) && !preList.isEmpty()) {
                    preList.forEach(obj2 -> {
                        String key = obj2.getReagentName() + "-" + obj2.getBathNumber();
                        preListMap.put(key, obj2);
                    });
                }

                // 遍历当前时间区间的列表
                for (ScreenConsumeDO obj1 : list) {
                    String key = obj1.getReagentName() + "-" + obj1.getBathNumber();
                    // 创建一个响应对象并设置相关属性
                    ScreenConsumeStatisticsRespVO respVO = new ScreenConsumeStatisticsRespVO();
                    respVO.setReagentName(obj1.getReagentName())
                            .setBathNumber(obj1.getBathNumber())
                            .setConsumption(obj1.getCurrentNumber());

                    // 从前一个时间区间的映射中获取对应的数据
                    ScreenConsumeDO obj2 = preListMap.get(key);
                    double percentage = 0.0;
                    // 如果前一个时间区间的数据存在并且当前消耗量不为0，计算消耗百分比
                    if (obj2 != null) {
                        if (obj2.getCurrentNumber() != 0) {
                            percentage = (1.0 * (obj1.getCurrentNumber() - obj2.getCurrentNumber())) / obj2.getCurrentNumber();
                        }
                    }

                    // 乘以 100 并保留两位小数
                    respVO.setConsumptionPercentage(Math.round(percentage * 100 * 100.0) / 100.0);

                    // 将结果添加到结果列表中
                    resultList.add(respVO);
                }

                // 返回去重后的结果列表
                return resultList.stream().distinct().toList();
            }
            // 如果当前时间区间的列表为空，返回空列表
            return new ArrayList<>();
        }
        // 如果创建时间参数为空或长度不符合要求，返回空列表
        return new ArrayList<>();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer decreaseScreenConsumeBatch(List<ScreenConsumeRecordDO> list) {
        if (list.isEmpty() || list == null) {
            throw exception(SCREEN_CONSUME_RECORD_LIST_NULL);
        }
        Integer successNum = 0;
        for (ScreenConsumeRecordDO screenConsumeRecordDO : list) {
            ScreenConsumeRecordDO one = screenConsumeRecordMapper.selectOne(ScreenConsumeRecordDO::getPadId, screenConsumeRecordDO.getPadId());
            ScreenConsumeDO screenConsumeDO = screenConsumeMapper.selectById(screenConsumeRecordDO.getConsumeId());
            if (screenConsumeDO.getCurrentNumber() - screenConsumeRecordDO.getChangeNumber() >= 0) {
            if (BeanUtil.isEmpty(one)){
                screenConsumeRecordDO.setId(null);
                screenConsumeRecordMapper.insert(screenConsumeRecordDO);
            }else {
                screenConsumeRecordDO.setId(one.getId());
                screenConsumeRecordMapper.updateById(screenConsumeRecordDO);
            }
            screenConsumeMapper.decreaseScreenConsume(screenConsumeRecordDO.getConsumeId(), screenConsumeRecordDO.getChangeNumber());
            successNum++;
            }
        }
        return successNum;
    }

    @Override
    public Integer getReagentTotalNum(Long reagentId,Long deptId) {
        return screenConsumeMapper.listConsumeByReagentId(reagentId,deptId);
    }
}