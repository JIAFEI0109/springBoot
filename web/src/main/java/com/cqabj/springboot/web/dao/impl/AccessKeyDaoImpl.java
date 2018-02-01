package com.cqabj.springboot.web.dao.impl;

import com.cqabj.springboot.model.entity.AccessKeyInfo;
import com.cqabj.springboot.web.dao.AccessKeyDao;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author fjia
 * @version V1.0 --2018/2/1-${time}
 */
@Repository
public class AccessKeyDaoImpl extends BaseDao implements AccessKeyDao {

    public AccessKeyDaoImpl(HibernateTemplate hibernateTemplate) {
        super(hibernateTemplate);
    }

    @Override
    public AccessKeyInfo getAccessKeyInfoById(String accessKeyId) {
        String[] keys = { "accessKeyId" };
        Object[] values = { accessKeyId };
        return criteriaExecuteUniqueResult(AccessKeyInfo.class, keys, values);
    }
}
