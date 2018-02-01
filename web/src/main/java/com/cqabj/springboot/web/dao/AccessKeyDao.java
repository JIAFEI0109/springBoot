package com.cqabj.springboot.web.dao;

import com.cqabj.springboot.model.entity.AccessKeyInfo;

/**
 * @author fjia
 * @version V1.0 --2018/2/1-${time}
 */
public interface AccessKeyDao extends CrudDao {

    /**
     * 根据密钥获取密文
     * @param accessKeyId 密钥id
     * @return AccessKeyInfo
     */
    AccessKeyInfo getAccessKeyInfoById(String accessKeyId);
}
