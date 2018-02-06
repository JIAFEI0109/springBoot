package com.cqabj.springboot.web.dao.impl;

import com.cqabj.springboot.model.common.IGlobalConstant;
import com.cqabj.springboot.utils.ObjectUtil;
import com.cqabj.springboot.web.dao.CrudDao;
import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.Type;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.orm.hibernate5.HibernateTemplate;

import java.util.List;
import java.util.Queue;

/**
 * Dao的基础类
 * @author fjia
 * @version V1.0 --2018/1/29-${time}
 */
abstract class BaseDao implements CrudDao {

    private HibernateTemplate hibernateTemplate;

    public BaseDao(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    @Override
    public void save(Object model) {
        hibernateTemplate.save(model);
    }

    @Override
    public void update(Object model) {
        hibernateTemplate.update(model);
    }

    @Override
    public void delete(Object model) {
        hibernateTemplate.delete(model);
    }

    @Override
    public <T> T get(Class clazz, Long id) {
        return ObjectUtil.typeConversion(hibernateTemplate.get(clazz, id));
    }

    @Override
    public void merge(Object model) {
        hibernateTemplate.merge(model);
    }

    @Override
    public void evict(Object obj) {
        hibernateTemplate.evict(obj);
    }

    @Override
    public <T> List<T> finAll(Class clazz) {
        return criteriaExecute(clazz);
    }

    public <T> List<T> criteriaExecute(Class clazz) {
        return criteriaExecuteList(clazz, IGlobalConstant.NO_PAGE_QUERY,
            IGlobalConstant.NO_PAGE_INDEX, null, null);
    }

    /**
     * 查询list集合
     * @param clazz 持久化集合
     * @param pageSize 分页大小
     * @param pageIndex 页码
     * @param keys 查询key 实体的属性名
     * @param values 查询条件
     * @param <T> 返回集合
     * @return List<T>
     */
    public <T> List<T> criteriaExecuteList(final Class clazz, final Integer pageSize,
                                           final Integer pageIndex, final String[] keys,
                                           final Object[] values) {
        return hibernateTemplate.execute(new HibernateCallback<List<T>>() {
            @Override
            public List<T> doInHibernate(Session session) throws HibernateException {
                Criteria criteria = session.createCriteria(clazz);
                if (pageIndex > -1 && pageSize > 0) {
                    criteria.setFirstResult(pageSize * pageIndex);
                    criteria.setMaxResults(pageIndex);
                }
                criteriaWhereCondition(criteria, keys, values);
                return ObjectUtil.typeConversion(criteria.list());
            }
        });
    }

    @Override
    public Long count(Class clazz) {
        return null;
    }

    public <T> T sqlExceuteList(final Class clazz, final String sql, final String[] keys,
                                final Object[] values, final Type[] types) {
        return hibernateTemplate.execute(new HibernateCallback<T>() {
            @Override
            public T doInHibernate(Session session) throws HibernateException {
                SQLQuery query = session.createSQLQuery(sql);
                whereCondition(query, keys, values, types);
                if (clazz != null) {
                    query.addEntity(clazz);
                }
                return ObjectUtil.typeConversion(query.uniqueResult());
            }
        });
    }

    /**
     *
     * @param clazz 持久化实体
     * @param keys 查询key实体的属性名
     * @param values 查询条件
     * @param <T> 返回实体
     * @return 实体集合
     */
    public <T> T criteriaExecuteUniqueResult(final Class clazz, final String[] keys,
                                             final Object[] values) {
        return hibernateTemplate.execute(new HibernateCallback<T>() {
            @Override
            public T doInHibernate(Session session) throws HibernateException {
                Criteria criteria = session.createCriteria(clazz);
                criteriaWhereCondition(criteria, keys, values);
                return null;
            }
        });
    }

    /**
     * criteria 方式拼接where条件
     * @param criteria criteria
     * @param keys keys
     * @param values values
     */
    private void criteriaWhereCondition(Criteria criteria, String[] keys, Object[] values) {
        if (keys != null && values != null) {
            for (int i = 0; i < keys.length; i++) {
                criteria.add(Restrictions.eq(keys[i], values[i]));
            }
        }
    }

    /**
     *
     * @param clazz 持久化对象
     * @param sql sql
     * @param keys 键
     * @param values 值
     * @param types 值类型
     * @param <T> 返回数据
     * @return 返回数据集合
     */
    public <T> List<T> sqlExecuteList(final Class clazz, final String sql, final String[] keys,
                                      final Object[] values, final Type[] types) {
        return hibernateTemplate.execute(new HibernateCallback<List<T>>() {
            @Override
            public List<T> doInHibernate(Session session) throws HibernateException {
                SQLQuery query = session.createSQLQuery(sql);
                whereCondition(query, keys, values, types);
                if (clazz != null) {
                    query.addEntity(clazz);
                }
                return ObjectUtil.typeConversion(query.list());
            }
        });
    }

    /**
     * 填充where值
     * @param query 查询对象
     * @param keys 键
     * @param values 值
     * @param types 值类型
     */
    private void whereCondition(Query query, String[] keys, Object[] values, Type[] types) {
        if (ArrayUtils.isNotEmpty(keys) && ArrayUtils.isNotEmpty(types)) {
            fillCondition(query, keys, values, types);
            return;
        }
        if (ArrayUtils.isNotEmpty(keys)) {
            fillCondition(query, keys, values);
            return;
        }
        if (ArrayUtils.isNotEmpty(types)) {
            fillCondition(query, values, types);
            return;
        }
        if (ArrayUtils.isNotEmpty(values)) {
            fillCondition(query, values);
        }
    }

    /**
     * @param query 查询对象
     * @param keys 键
     * @param values 值
     * @param types 值类型
     */
    private void fillCondition(Query query, String[] keys, Object[] values, Type[] types) {
        for (int i = 0; i < keys.length; i++) {
            query.setParameter(keys[i], values[i], types[i]);
        }
    }

    /**
     * @param query 查询对象
     * @param keys 键
     * @param values 值
     */
    private void fillCondition(Query query, String[] keys, Object[] values) {
        for (int i = 0; i < values.length; i++) {
            query.setParameter(keys[i], values[i]);
        }
    }

    /**
     * @param query 查询对象
     * @param values 值
     * @param types 值类型
     */
    private void fillCondition(Query query, Object[] values, Type[] types) {
        for (int i = 0; i < values.length; i++) {
            query.setParameter(i, values[i], types[i]);
        }
    }

    /**
     * @param query 查询对象
     * @param values 值
     */
    private void fillCondition(Query query, Object[] values) {
        for (int i = 0; i < values.length; i++) {
            query.setParameter(i, values[i]);
        }
    }

}
