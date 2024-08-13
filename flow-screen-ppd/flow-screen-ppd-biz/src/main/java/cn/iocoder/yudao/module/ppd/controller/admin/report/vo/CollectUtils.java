package cn.iocoder.yudao.module.ppd.controller.admin.report.vo;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;

import java.util.*;

@Data
public class CollectUtils<T> {
    // 私有静态实例
    private  CollectUtils<T> instance;

    // 私有构造函数防止外部实例化
    public CollectUtils() {
    }

    // 公共静态方法返回唯一实例
   /* public static <T> CollectUtils<T> getInstance() {
        if (instance == null) {
            instance = new CollectUtils<>();
        }
        return instance;
    }*/
    public  List<List<T>> removeDuplicates(List<List<T>> ito){
        Set<T> hashSet = new HashSet<>(ito.get(0));
        List<List<T>> result=new ArrayList<>(ito.size());
        result.add(ito.get(0));
        for (int i = 1; i < ito.size(); i++) {
            Collection<T> intersection = CollectionUtil.intersection(hashSet, ito.get(i));
//            ito.set(i,new ArrayList<>(CollectionUtil.disjunction(ito.get(i),intersection)));
            result.add(new ArrayList<>(CollectionUtil.disjunction(ito.get(i),intersection)));
            hashSet.addAll(ito.get(i));
        }
        return result;
    }
}
