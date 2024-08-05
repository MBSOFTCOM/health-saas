package cn.iocoder.yudao.module.ppd.service.screenrepeatperson;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.ppd.controller.admin.screenrepeatperson.vo.ScreenRepeatPersonPageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenrepeatperson.vo.ScreenRepeatPersonSaveReqVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenpersonrealsituation.ScreenPersonDO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenrepeatperson.ScreenRepeatPersonDO;
import cn.iocoder.yudao.module.ppd.dal.mysql.screendistrict.ScreenDistrictMapper;
import cn.iocoder.yudao.module.ppd.dal.mysql.screenpersonrealsituation.ScreenPersonMapper;
import cn.iocoder.yudao.module.ppd.dal.mysql.screenrepeatperson.ScreenRepeatPersonMapper;
import cn.iocoder.yudao.module.system.api.dict.DictDataApi;
import cn.iocoder.yudao.module.system.api.dict.dto.DictDataRespDTO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.cd.enums.ErrorCodeConstants.SCREEN_REPEAT_PERSON_NOT_EXISTS;

/**
 * 重复筛查人员管理 Service 实现类
 *
 * @author 侯卿
 */
@Service
@Validated
public class ScreenRepeatPersonServiceImpl implements ScreenRepeatPersonService {

    @Resource
    private ScreenRepeatPersonMapper screenRepeatPersonMapper;
    @Resource
    private DictDataApi dictDataApi;
    @Resource
    private ScreenPersonMapper screenPersonMapper;
    @Resource
    private ScreenDistrictMapper screenDistrictMapper;

    @Override
    public Long createScreenRepeatPerson(ScreenRepeatPersonSaveReqVO createReqVO) {
        // 插入
        ScreenRepeatPersonDO screenRepeatPerson = BeanUtils.toBean(createReqVO, ScreenRepeatPersonDO.class);
        screenRepeatPersonMapper.insert(screenRepeatPerson);
        // 返回
        return screenRepeatPerson.getId();
    }

    @Override
    public void updateScreenRepeatPerson(ScreenRepeatPersonSaveReqVO updateReqVO) {
        // 校验存在
        validateScreenRepeatPersonExists(updateReqVO.getId());
        // 更新
        ScreenRepeatPersonDO updateObj = BeanUtils.toBean(updateReqVO, ScreenRepeatPersonDO.class);
        Integer firstType = updateObj.getFirstType();
        Integer moreType = updateObj.getMoreType();

        // 根据第一个类型判断并更新更多类型
        if (firstType == 4) {
            moreType += 4;
        }

        // 判断学生和僧尼
        String resolvedMoreType = resolveMoreTypeToString(moreType);
        if (moreType != null) {
            if (!resolvedMoreType.contains("学生")) {
                updateObj.setClassroom("").setIsNewStudent(0);
            }
        }
        updateObj.setMoreType(moreType);
        screenRepeatPersonMapper.updateById(updateObj);
    }

    @Override
    public String resolveMoreTypeToString(Integer moreType) {
        if (moreType == null || moreType == 0) {
            return "";
        }

        List<DictDataRespDTO> dictList = dictDataApi.getDictDataList("tb_more_people_type");

        Map<Integer, String> typeMap = new HashMap<>();

        for (DictDataRespDTO obj : dictList) {
            typeMap.put(Integer.valueOf(obj.getValue()), obj.getLabel());
        }

        String result = typeMap.entrySet().stream()
                .filter(entry -> (moreType & entry.getKey()) == entry.getKey())
                .map(Map.Entry::getValue)
                .collect(Collectors.joining("，"));

        return result;
    }

    @Override
    public void deleteScreenRepeatPerson(Long id) {
        // 校验存在
        validateScreenRepeatPersonExists(id);
        // 删除
        screenRepeatPersonMapper.deleteById(id);
    }

    private void validateScreenRepeatPersonExists(Long id) {
        if (screenRepeatPersonMapper.selectById(id) == null) {
            throw exception(SCREEN_REPEAT_PERSON_NOT_EXISTS);
        }
    }

    @Override
    public ScreenRepeatPersonDO getScreenRepeatPerson(Long id) {
        return screenRepeatPersonMapper.selectById(id);
    }

    @Override
    public PageResult<ScreenRepeatPersonDO> getScreenRepeatPersonPage(ScreenRepeatPersonPageReqVO pageReqVO) {
        return screenRepeatPersonMapper.selectPage(pageReqVO);
    }

    @Override
    public Boolean getIsRemainRepeatPerson() {
        Integer count = screenRepeatPersonMapper.getIsRemainRepeatPerson();
        System.out.println(count);
        return count != null && count != 0;
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean isExist(Long id) {
        try {
            // 查询重复人员信息
            ScreenRepeatPersonDO obj = screenRepeatPersonMapper.selectById(id);
            // 查询是否存在符合条件的重复记录
            Integer count = screenRepeatPersonMapper.isExist(obj.getIdNum(), obj.getYear(), obj.getScreenType());
            // 如果 count 为 null 或者为 0，则表示不存在符合条件的重复记录，返回 true
            return count != null && count != 0;
        } catch (Exception e) {
            // 捕获任何异常，打印异常信息，根据业务需求可以选择抛出异常或者返回 false
            e.printStackTrace(); // 可以根据需要记录日志
            return false;
        }
    }


    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Boolean recoverData(Long id) {
        try {
            // 找到重复人员信息
            ScreenRepeatPersonDO obj = screenRepeatPersonMapper.selectById(id);
            // 新建 摸底人员 对象
            ScreenPersonDO screenPersonDO = new ScreenPersonDO();
            // 忽略 id 拷贝
            CopyOptions options = CopyOptions.create().setIgnoreProperties("id");
            BeanUtil.copyProperties(obj, screenPersonDO, options);

            List<String> screenIdList = screenPersonMapper.getMaxScreenId(screenPersonDO.getYear(), screenPersonDO.getTown());
            // 根据镇名称查询对应的区域代码
            String code = screenDistrictMapper.selectByName(screenPersonDO.getTown());
            // 找到该乡镇最大的筛查编号
            String maxScreenId = constructMaxScreenId(screenIdList, code, screenPersonDO.getYear(), screenPersonDO.getScreenType());
            // 生成筛查编号
            String screenId = makeScreenId(maxScreenId, code);
            screenPersonDO.setScreenId(screenId);
            // 摸底库中插入重复人员信息
            screenPersonMapper.insert(screenPersonDO);
            // 重复人员 删除
            screenRepeatPersonMapper.deleteById(id);
            // 所有步骤按顺序执行完毕，返回true
            return true;
        } catch (Exception e) {
            // 捕获任何异常，返回false
            e.printStackTrace(); // 可以根据需要记录日志
            return false;
        }
    }

    /**
     * 找到该乡镇最大的筛查编号
     */
    public static String constructMaxScreenId(List<String> screenIdList, String code, int year, int screenType) {
        // 初始化最大个人编号为 0
        int maxPersonalId = 0;
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
}