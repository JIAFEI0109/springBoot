package com.cqabj.springboot.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密
 * @author fjia
 * @version V1.0 --2018/1/31-${time}
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DigestPass {
    private static MessageDigest messageDigest;

    /**
     * 获取MD5加密
     * @param tempString 要加密的字符串
     * @return 加密后的字符串
     */
    public static String getDigestPassWord(String tempString) {
        byte[] args = new byte[0];

        try {
            //生成MessageDigest对象,传入所用算法的参数MD5
            messageDigest = messageDigest.getInstance("MD5");
            //使用getBytes()方法生成字符串数组
            messageDigest.update(tempString.concat("cqabj_MD5").getBytes());
            //执行MessageDigest对象的digest()方法完成计算,计算的结果通过字符类型数组返回
            args = messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            log.error(StringUtils.outPutException(e));
        } finally {
            messageDigest.reset();
        }

        //将结果转换为字符串
        StringBuilder result = new StringBuilder();
        for (byte arg : args) {
            Integer number = (0x000000ff & arg) | 0xffffff00;
            result.append(Integer.toHexString(number).substring(6));
        }
        return result.toString();
    }
}
