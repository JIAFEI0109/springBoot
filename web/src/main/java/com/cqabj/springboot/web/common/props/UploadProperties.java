package com.cqabj.springboot.web.common.props;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author fjia
 * @version V1.0 --2018/1/26-${time}
 */
@Data
@ToString
@Component
@PropertySource("classpath:config/upload.properties")
@ConfigurationProperties(prefix = "upload", ignoreUnknownFields = false)
public class UploadProperties {

	private FileEntiy file;

	private MappingEntity mapping;

	private ThumbnailEntity thumbnail;


	/**
	 * 文件目录属性
	 */
	@Data
	@ToString
	public static class FileEntiy {

		/**
		 * 一级目录
		 */
		private String rootPath;

		/**
		 * 二级临时目录
		 */
		private String tempPath;

		/**
		 * 三级正式目录
		 */
		private String formalPath;

		/**
		 * 三级图片目录
		 */
		private String imagePath;

		/**
		 * 三级文件目录
		 */
		private String filePath;

		/**
		 * 三级QR图片目录
		 */
		private String qrimagePath;
	}

	/**
	 * 缩略图
	 */
	@Data
	@ToString
	public static class ThumbnailEntity {

		/**
		 * 图片高度
		 */
		private Integer height;

		/**
		 * 图片宽度
		 */
		private Integer width;

		/**
		 * 缩略图前缀
		 */
		private String prefix;
	}

	/**
	 * 映射关系
	 */
	@Data
	@ToString
	public static class MappingEntity {

		/**
		 * 图片映射路径
		 */
		private String image;

		/**
		 * 文件映射路径
		 */
		private String file;

		/**
		 * 图片临时目录映射
		 */
		private String tempImage;
	}
}
