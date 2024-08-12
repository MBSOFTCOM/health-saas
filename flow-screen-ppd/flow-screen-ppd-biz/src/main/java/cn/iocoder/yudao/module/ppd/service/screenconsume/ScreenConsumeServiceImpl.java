package cn.iocoder.yudao.module.ppd.service.screenconsume;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.ppd.controller.admin.screenconsume.vo.ScreenConsumeImportVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenconsume.vo.ScreenConsumePageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenconsume.vo.ScreenConsumeSaveReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenreagent.vo.ScreenReagentImportRespVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenconsume.ScreenConsumeDO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenconsumerecord.ScreenConsumeRecordDO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenreagent.ScreenReagentDO;
import cn.iocoder.yudao.module.ppd.dal.mysql.screenconsume.ScreenConsumeMapper;
import cn.iocoder.yudao.module.ppd.dal.mysql.screenconsumerecord.ScreenConsumeRecordMapper;
import cn.iocoder.yudao.module.ppd.dal.mysql.screenreagent.ScreenReagentMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createScreenConsume(ScreenConsumeSaveReqVO createReqVO) {
        // 插入
        ScreenConsumeDO screenConsume = BeanUtils.toBean(createReqVO, ScreenConsumeDO.class);

        Integer count =
                screenConsumeMapper.isExist(screenConsume.getReagentId(),
                        screenConsume.getConsumeOrder(), screenConsume.getBathNumber(),
                        screenConsume.getIndate(), screenConsume.getManufactureDate());

        if (count > 0){
            throw exception(SCREEN_CONSUME_IS_EXISTS);
        }

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

        Integer count =
                screenConsumeMapper.isExist(updateObj.getReagentId(),
                        updateObj.getConsumeOrder(), updateObj.getBathNumber(),
                        updateObj.getIndate(), updateObj.getManufactureDate());

        if (count > 1){
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
        return screenConsumeMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ScreenConsumeDO> getUsableScreenConsume(ScreenConsumePageReqVO pageReqVO) {
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

        if (screenConsumeDO.getCurrentNumber() - number >= 0){
            Integer count = screenConsumeMapper.decreaseScreenConsume(id, number);

            int insertCount =
                    screenConsumeRecordMapper.insert(
                            new ScreenConsumeRecordDO()
                                    .setConsumeId(id)
                                    .setChangeNumber(number)
                                    .setType(3));

            return count > 0 && insertCount > 0;
        }else {
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

        if (filteredList.isEmpty()) {
            return screenReagentImportRespVO;
        }

        for (ScreenConsumeImportVO obj : filteredList) {
            if (ObjectUtil.isNull(obj.getReagentName())){
                failureSpecification.put(count, "没有选择试剂");
            }else if (ObjectUtil.isNull(obj.getConsumeOrder())){
                failureSpecification.put(count, "消耗序位为空");
            }else if (ObjectUtil.isNull(obj.getBathNumber())){
                failureSpecification.put(count, "批次号为空");
            }else if (ObjectUtil.isNull(obj.getInboundNumber())){
                failureSpecification.put(count, "入库量为空");
            }else if (ObjectUtil.isNull(obj.getManufactureDate())){
                failureSpecification.put(count, "生产日期为空");
            }else if (ObjectUtil.isNull(obj.getIndate())){
                failureSpecification.put(count, "有效期为空");
            }else {
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
                                obj.getIndate(), localDateTime);
                if (isExist > 0){
                    failureSpecification.put(count, "该消耗管理已存在");
                }else {
                    ScreenReagentDO screenReagentDO =
                            screenReagentMapper.selectByName(obj.getReagentName());

                    ScreenConsumeDO screenConsumeDO = new ScreenConsumeDO();
                    BeanUtil.copyProperties(obj, screenConsumeDO);
                    screenConsumeDO.setReagentId(screenReagentDO.getId())
                            .setReagentType(screenReagentDO.getType())
                            .setUsable(screenReagentDO.getUsable())
                            .setThreshold(screenReagentDO.getThreshold())
                            .setReagentSpecsNum(screenReagentDO.getReagentSpecsNum())
                            .setCurrentNumber(obj.getInboundNumber())
                            .setManufactureDate(localDateTime);

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

}