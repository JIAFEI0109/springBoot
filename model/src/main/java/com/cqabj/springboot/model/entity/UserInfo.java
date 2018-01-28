package com.cqabj.springboot.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;


/**
 * 用户个人信息
 * @author fjia
 * Created by cqabj on 2018/1/28.
 */
@Data
@Entity
@ToString
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_info")
public class UserInfo {

	@Id
	@Column(name = "u_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long uId;

	@Basic
	@Column(name = "user_name")
	private String userName;

	@Basic
	@Column(name = "login_name")
	private String loginName;

	@Basic
	@Column(name = "pwd")
	private String pwd;
}
