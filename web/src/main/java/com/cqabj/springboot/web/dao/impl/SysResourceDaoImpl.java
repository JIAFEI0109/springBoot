package com.cqabj.springboot.web.dao.impl;

import com.cqabj.springboot.model.entity.SysResources;
import com.cqabj.springboot.web.dao.SysResourceDao;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author fjia
 * @version V1.0 --2018/1/31-${time}
 */
@Repository
public class SysResourceDaoImpl extends BaseDao implements SysResourceDao {

    public SysResourceDaoImpl(HibernateTemplate hibernateTemplate) {
        super(hibernateTemplate);
    }

    @Override
    public List<SysResources> getResourcesByUid(Long uId) {
        String sql = "SELECT e.* FROM user_info a LEFT JOIN sys_user_roles b ON a.u_id = b.user_id LEFT JOIN sys_roles c ON b.role_id = c.role_id AND c.disable_flag = 1 LEFT JOIN sys_roles_resources d ON c.role_id = d.resource_id LEFT JOIN sys_resources e ON d.resource_id = e.resource_id WHERE a.u_id =:uid";
        String[] keys = { "uId" };
        Object[] values = { uId };
        return sqlExecuteList(SysResources.class, sql, keys, values, null);
    }
}
