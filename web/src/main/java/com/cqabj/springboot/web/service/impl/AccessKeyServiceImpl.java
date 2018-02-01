package com.cqabj.springboot.web.service.impl;

import com.cqabj.springboot.model.entity.AccessKeyInfo;
import com.cqabj.springboot.web.dao.AccessKeyDao;
import com.cqabj.springboot.web.service.AccessKeyService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 签名密钥实现类
 * @author fjia
 * @version V1.0 --2018/2/1-${time}
 */
@Slf4j
@Service
@AllArgsConstructor
public class AccessKeyServiceImpl extends BaseService implements AccessKeyService {

	private AccessKeyDao accessKeyDao;

	@Override
    public AccessKeyInfo getAccessKeyInfoById(String accessKeyId) {
        return accessKeyDao.getAccessKeyInfoById(accessKeyId);
    }
}
