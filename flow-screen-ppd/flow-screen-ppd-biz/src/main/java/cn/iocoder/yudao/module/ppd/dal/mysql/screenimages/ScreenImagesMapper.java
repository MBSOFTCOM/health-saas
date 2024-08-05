package cn.iocoder.yudao.module.ppd.dal.mysql.screenimages;


import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenimages.ScreenImagesDO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 移动端各组离线图片信息 Mapper
 *
 * @author 芋道源码
 */
@Mapper
@InterceptorIgnore(tenantLine = "true")
public interface ScreenImagesMapper extends BaseMapperX<ScreenImagesDO> {

    String selectLastTimeUrl(@Param("personId") Long personId,
                             @Param("type") int type,
                             @Param("year") Integer year,
                             @Param("screenType") Integer screenType);

    Long selectIsNull(@Param("personId")Long personId,
                      @Param("screenId")String screenId,
                      @Param("imageType")Integer imageType,
                      @Param("screenOrder")Integer screenOrder,
                      @Param("year")Integer year,
                      @Param("screenType")Integer screenType);
}