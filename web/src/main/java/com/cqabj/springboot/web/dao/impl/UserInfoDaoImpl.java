package com.cqabj.springboot.web.dao.impl;

import com.cqabj.springboot.model.entity.UserInfo;
import com.cqabj.springboot.web.dao.UserInfoDao;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author fjia
 * @version V1.0 --2018/1/29-${time}
 */
@Repository
public class UserInfoDaoImpl extends BaseDao implements UserInfoDao {

    public UserInfoDaoImpl(HibernateTemplate hibernateTemplate) {
        super(hibernateTemplate);
    }

    @Override
    public UserInfo getUserInfoByName(String userName) {
        String sql = "SELECT * FROM user_info a WHERE a.user_name =:userName";
        String[] keys = { "userName" };
        Object[] values = { userName };
        return sqlExceuteList(UserInfo.class, sql, keys, values, null);
    }

    @Override
    public UserInfo getUserInfoByUID(Long uId) {
        String[] keys = { "uId" };
        Object[] values = { uId };
        return criteriaExecuteUniqueResult(UserInfo.class, keys, values);
    }
}
