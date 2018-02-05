package com.cqabj.springboot.web.dao;

import com.cqabj.springboot.model.entity.SysResources;

import java.util.List;

/**
 * @author fjia
 * @version V1.0 --2018/1/29-${time}
 */
public interface SysResourceDao extends CrudDao {

    /**
     * 通过uId回去资源数据
     * @param uId uid
     * @return List<SysResources>
     */
    List<SysResources> getResourcesByUid(Long uId);


    /**
     * 获取客户端所有资源
     * @return List<SysResources>
     */
    List<SysResources> getClientAllResources();
}
