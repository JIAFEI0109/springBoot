package com.cqabj.springboot.web.dao.impl;

import com.cqabj.springboot.utils.ObjectUtil;
import com.cqabj.springboot.web.dao.CrudDao;
import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
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
        return null;
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
                if (clazz!=null){
                    query.addEntity(clazz);
                }
                return ObjectUtil.typeConversion(query.uniqueResult());
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
        }
    }

    /**
     *
     * @param query
     * @param keys
     * @param values
     * @param types
     */
    private void fillCondition(Query query, String[] keys, Object[] values, Type[] types) {
        for (int i = 0; i < keys.length; i++) {
            query.setParameter(keys[i], values[i], types[i]);
        }
    }

}
