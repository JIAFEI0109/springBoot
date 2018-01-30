package com.cqabj.springboot.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 系统资源表
 * @author fjia
 * @version V1.0 --2018/1/29-${time}
 */
@Data
@Entity
@ToString
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "sys_resources")
public class SysResources implements Serializable {

	private static final long serialVersionUID = 7288120982751110476L;


}
