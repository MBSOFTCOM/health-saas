package cn.iocoder.yudao.module.ppd.service.screenreagent;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.ppd.controller.admin.screenreagent.vo.ScreenReagentImportRespVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenreagent.vo.ScreenReagentImportVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenreagent.vo.ScreenReagentPageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenreagent.vo.ScreenReagentSaveReqVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenconsume.ScreenConsumeDO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenreagent.ScreenReagentDO;
import cn.iocoder.yudao.module.ppd.dal.mysql.screenconsume.ScreenConsumeMapper;
import cn.iocoder.yudao.module.ppd.dal.mysql.screenreagent.ScreenReagentMapper;
import cn.iocoder.yudao.module.system.api.dict.DictDataApi;
import cn.iocoder.yudao.module.system.api.dict.dto.DictDataRespDTO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.cd.enums.ErrorCodeConstants.SCREEN_REAGENT_Is_EXISTS;
import static cn.iocoder.yudao.module.cd.enums.ErrorCodeConstants.SCREEN_REAGENT_NOT_EXISTS;

/**
 * 试剂 Service 实现类
 *
 * @author 侯卿
 */
@Service
@Validated
public class ScreenReagentServiceImpl implements ScreenReagentService {

    @Resource
    private ScreenReagentMapper screenReagentMapper;
    @Resource
    private DictDataApi dictDataApi;
    @Resource
    private ScreenConsumeMapper screenConsumeMapper;

    @Override
    public Long createScreenReagent(ScreenReagentSaveReqVO createReqVO) {
        // 插入
        ScreenReagentDO screenReagent = BeanUtils.toBean(createReqVO, ScreenReagentDO.class);

        // 判断是否已经有这种试剂了
        /*Integer count = screenReagentMapper.selectIsExist(screenReagent.getName(), screenReagent.getType(),
                screenReagent.getReagentSpecsNum(), screenReagent.getTiter(), screenReagent.getPotencyUnit(),
                screenReagent.getSpecification(), screenReagent.getSpecificationUnit(), screenReagent.getPackageUnit(),
                screenReagent.getManufacturer(), screenReagent.getThreshold());*/
        Integer count = screenReagentMapper.selectIsExist(screenReagent.getName());
        if (count > 0){
            throw exception(SCREEN_REAGENT_Is_EXISTS);
        }

        screenReagentMapper.insert(screenReagent);
        // 返回
        return screenReagent.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateScreenReagent(ScreenReagentSaveReqVO updateReqVO) {
        // 校验试剂是否存在
        validateScreenReagentExists(updateReqVO.getId());

        // 将请求对象转换为数据库实体对象
        ScreenReagentDO updateObj = BeanUtils.toBean(updateReqVO, ScreenReagentDO.class);

        // 检查试剂是否已经存在，避免重复
        if (isReagentExists(updateObj)) {
            throw exception(SCREEN_REAGENT_Is_EXISTS);
        }

        // 更新试剂信息
        screenReagentMapper.updateById(updateObj);

        // 更新与试剂相关的消耗管理
        updateConsumeRecords(updateObj);
    }

    /**
     * 检查试剂是否已经存在
     *
     * @param updateObj 需要检查的试剂对象
     * @return 如果试剂已存在，则返回 true，否则返回 false
     */
    private boolean isReagentExists(ScreenReagentDO updateObj) {
        /*Integer count = screenReagentMapper.selectIsExist(
                updateObj.getName(), updateObj.getType(),
                updateObj.getReagentSpecsNum(), updateObj.getTiter(),
                updateObj.getPotencyUnit(), updateObj.getSpecification(),
                updateObj.getSpecificationUnit(), updateObj.getPackageUnit(),
                updateObj.getManufacturer(), updateObj.getThreshold()
        );*/
        Integer count = screenReagentMapper.selectIsExist(updateObj.getName());
        return count > 1;
    }

    /**
     * 更新与试剂相关的消耗管理
     *
     * @param updateObj 更新后的试剂对象
     */
    private void updateConsumeRecords(ScreenReagentDO updateObj) {
        // 查询所有与试剂相关的消耗管理
        List<ScreenConsumeDO> consumeList = screenConsumeMapper.selcetByReagentId(updateObj.getId());

        if (consumeList.size() > 0){
            // 更新每个消耗管理中的试剂信息
            List<ScreenConsumeDO> updatedList = consumeList.stream()
                    .peek(obj -> {
                        obj.setReagentName(updateObj.getName())
                                .setReagentType(updateObj.getType())
                                .setReagentSpecsNum(updateObj.getReagentSpecsNum())
                                .setThreshold(updateObj.getThreshold());
                    })
                    .collect(Collectors.toList());

            // 批量更新消耗管理
            if (!updatedList.isEmpty()) {
                screenConsumeMapper.updateBatch(updatedList);
            }
        }
    }



    @Override
    public void deleteScreenReagent(Long id) {
        // 校验存在
        validateScreenReagentExists(id);
        // 删除
        screenReagentMapper.deleteById(id);
    }

    private void validateScreenReagentExists(Long id) {
        if (screenReagentMapper.selectById(id) == null) {
            throw exception(SCREEN_REAGENT_NOT_EXISTS);
        }
    }

    @Override
    public ScreenReagentDO getScreenReagent(Long id) {
        return screenReagentMapper.selectById(id);
    }

    @Override
    public PageResult<ScreenReagentDO> getScreenReagentPage(ScreenReagentPageReqVO pageReqVO) {
        return screenReagentMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ScreenReagentDO> getUsableReagent(ScreenReagentPageReqVO pageReqVO) {
        return screenReagentMapper.selectUsable(pageReqVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean forbidScreenReagent(Long id) {
        Integer count = screenReagentMapper.forbidReagent(id);
        Integer count2 = screenConsumeMapper.forbid(id);
        return count > 0 && count2 > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean recoverScreenReagent(Long id) {
        Integer count = screenReagentMapper.recoverReagent(id);
        Integer count2 = screenConsumeMapper.recover(id);
        return count > 0 && count2 > 0;
    }

    @Override
    public List<ScreenReagentImportVO> createSampleData() {
        return List.of(
                ScreenReagentImportVO.builder().name("PPD").type(2).titer(BigDecimal.valueOf(21.00))
                        .potencyUnit(1).specification(BigDecimal.valueOf(1.00)).specificationUnit(1)
                        .packageUnit(1).manufacturer("上海科前生物科技有限公司").threshold(10).reagentSpecsNum(10)
                        .build()
        );
    }

    @Override
    public void addSelectedData(String dictType, int index, Map<Integer, List<String>> selectedData) {
        List<DictDataRespDTO> dictList = dictDataApi.getDictDataList(dictType);
        if (dictList.size() > 0) {
            selectedData(index, dictList, selectedData);
        }
    }

    public void selectedData(Integer index, List<DictDataRespDTO> dictDataRespDTOS, Map<Integer, List<String>> selectedData) {
        if (dictDataRespDTOS != null) {
            List<String> dataList = dictDataRespDTOS.stream().map(DictDataRespDTO::getLabel).collect(Collectors.toList());
            if (!dataList.isEmpty()) {
                selectedData.put(index, dataList);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ScreenReagentImportRespVO importReagent(List<ScreenReagentImportVO> list) {

        // 使用 Stream 过滤空对象
        List<ScreenReagentImportVO> filteredList = list.stream()
                .filter(vo -> !vo.isEmpty(vo)).toList();

        // 批量导入列表
        List<ScreenReagentDO> batchInsert = new ArrayList<>();
        // 导入成功的列表
        List<String> createSpecification = new ArrayList<>();
        //导入失败的列表
        Map<Integer, String> failureSpecification = new HashMap<>();
        ScreenReagentImportRespVO screenReagentImportRespVO = ScreenReagentImportRespVO.builder().build();
        // 记录顺序
        Integer count = 1;

        if (filteredList.isEmpty()) {
            return screenReagentImportRespVO;
        }

        for (ScreenReagentImportVO obj : filteredList) {
            if (ObjectUtil.isNull(obj.getName())) {
                failureSpecification.put(count, "试剂名称为空");
            } else if (ObjectUtil.isNull(obj.getType())) {
                failureSpecification.put(count, "试剂类型为空");
            } else if (ObjectUtil.isNull(obj.getReagentSpecsNum())) {
                failureSpecification.put(count, "转换系数（人次）为空");
            } else if (ObjectUtil.isNull(obj.getTiter())) {
                failureSpecification.put(count, "效价为空");
            } else if (ObjectUtil.isNull(obj.getPotencyUnit())) {
                failureSpecification.put(count, "效价单位为空");
            } else if (ObjectUtil.isNull(obj.getSpecification())) {
                failureSpecification.put(count, "规格为空");
            } else if (ObjectUtil.isNull(obj.getSpecificationUnit())) {
                failureSpecification.put(count, "规格单位为空");
            } else if (ObjectUtil.isNull(obj.getPackageUnit())) {
                failureSpecification.put(count, "包装单位为空");
            } else if (ObjectUtil.isNull(obj.getManufacturer())) {
                failureSpecification.put(count, "供应商为空");
            } else if (ObjectUtil.isNull(obj.getThreshold())) {
                failureSpecification.put(count, "库存预警值为空");
            } else {
                /*Integer isExist = screenReagentMapper.selectIsExist(obj.getName(), obj.getType(), obj.getReagentSpecsNum(),
                        obj.getTiter(), obj.getPotencyUnit(), obj.getSpecification(), obj.getSpecificationUnit(),
                        obj.getPackageUnit(), obj.getManufacturer(), obj.getThreshold());*/
                Integer isExist = screenReagentMapper.selectIsExist(obj.getName());
                if (isExist > 0){
                    failureSpecification.put(count, "该试剂已存在");
                }else {
                    batchInsert.add(BeanUtils.toBean(obj, ScreenReagentDO.class));
                    createSpecification.add("");
                }
            }
            count++;
        }

        // 批量导入试剂
        if (batchInsert.size() > 0){
            Collection<ScreenReagentDO> distinctList = new HashSet<>(batchInsert);
            screenReagentMapper.insertBatch(distinctList);
        }

        return screenReagentImportRespVO.setCreateSpecification(createSpecification)
                                        .setFailureSpecification(failureSpecification);
    }

    @Override
    public List<ScreenReagentDO> getReagentList() {
        return screenReagentMapper.getReagentList();
    }

    @Override
    public List<String> getReagentList2() {
        return screenReagentMapper.getReagentList2();
    }



}