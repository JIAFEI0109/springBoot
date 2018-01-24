package com.cqabj.springboot.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

/**
 * 图像相关工具类
 * @author fjia
 * @version V1.0 --2018/1/22-${time}
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PicUtils {

    /**
     * 将文件拷贝到指定目录
     * @param fileName 文件名
     * @param path 指定的路径
     */
    public static void copyPic(String fileName, String path) {
        Calendar calendar = Calendar.getInstance();
        String dateString = DateUtils.changeDateToStr(calendar.getTime(),
            DateUtils.DAY_DATE_PATTEN);
        String storePath = path + dateString + "/" + fileName;

        //保存图片路径(相对路径)
        String photoPath = StringUtils.getPropByName("photoTemporaryPath",
            "/filePath/path.properties");
        String pathString = photoPath + storePath;
        //临时文件路径
        String photoTemporaryPath = StringUtils.getPropByName("photoTemporaryPath",
            "/filePath/path" + ".properties");
        String temporaryPath = photoTemporaryPath + dateString + "/" + fileName;
        //读取临时文件
        File file = new File(temporaryPath);
        try (FileOutputStream fos = new FileOutputStream(pathString);
                InputStream in = new FileInputStream(file)) {
            if (file.exists()) {
                File file1 = new File(photoPath + path + dateString);
                if (!file1.exists()) {
                    file1.mkdirs();
                }
                byte[] buff = new byte[1024];
                int len;
                while ((len = in.read(buff)) > 0) {
                    fos.write(buff, 0, len);
                }
                fos.flush();
            }

        } catch (IOException e) {
            log.error("图片保存信息" + StringUtils.outPutException(e));
        }

    }
}
