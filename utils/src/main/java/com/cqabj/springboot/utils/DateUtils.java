package com.cqabj.springboot.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sun.misc.Cache;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.SimpleFormatter;

/**
 * @author fjia
 * @version V1.0 --2018/1/22-${time}
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtils {

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static final String FULL_DATE_PATTEN   = "yyyy-MM-dd HH:mm:ss";
    /**
     * yyyy-MM-dd HH:mm
     */
    public static final String DATW_MINUTE_PATTEN = "yyyy-MM-dd HH:mm";
    /**
     * yyyy-MM-dd
     */
    public static final String DAY_DATE_PATTEN    = "yyyy-MM-dd";

    /**
     * 把Date转成String
     * @param date 传入Date类型的时间
     * @param patten 传入的格式如("yyyy-MM-dd HH:mm:ss")
     * @return String
     */
    public static String changeDateToStr(Date date, String patten) {
        SimpleDateFormat sdf = new SimpleDateFormat(patten);
        String ret = "";
        try {
            if (null != date) {
                ret = sdf.format(date);
            }
        } catch (Exception e) {
            log.error(StringUtils.outPutException(e));
        }
        return ret;
    }
}
