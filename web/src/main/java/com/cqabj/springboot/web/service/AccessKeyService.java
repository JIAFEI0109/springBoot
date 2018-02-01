package com.cqabj.springboot.web.service;

import com.cqabj.springboot.model.entity.AccessKeyInfo;

/**
 * 签名密钥
 * @author fjia
 * @version V1.0 --2018/2/1-${time}
 */
public interface AccessKeyService {

    /**
     * 根据密钥获取密文
     * @param accessKeyId 密钥id
     * @return AccessKeyInfo
     */
    AccessKeyInfo getAccessKeyInfoById(String accessKeyId);


}
