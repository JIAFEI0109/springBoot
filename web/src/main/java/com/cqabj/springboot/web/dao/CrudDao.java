package com.cqabj.springboot.web.dao;

import java.util.List;

/**
 * @author fjia
 * Created by cqabj on 2018/1/28.
 */
public interface CrudDao {

    /**
     * 保存model
     * @param model 传入model
     */
    void save(Object model);

    /**
     * 更新model
     * @param model 需要更新的持久化实体
     */
    void update(Object model);

    /**
     * 删除model
     * @param model 传入的model
     */
    void delete(Object model);

    /**
     * get方法通过clazz以及主键id获取到对应的实体
     * @param clazz 持久化实体类
     * @param id 持久化实体的唯一标识
     * @return T 持久实体
     */
    <T> T get(Class clazz, Long id);

    /**
     * 合并model
     * @param model 传入的model
     */
    void merge(Object model);

    /**
     * 将持久化对象转换为托管对象
     * @param obj 传入obj
     */
    void evict(Object obj);

    /**
     * 对象全部集合
     * @param clazz 持久对象
     * @return 对象全部集合
     */
    <T> List<T> finAll(Class clazz);

    /**
     * 统计整个表
     * @param clazz 实体
     * @return 行数
     */
    Long count(Class clazz);

}
