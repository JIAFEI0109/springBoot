package com.cqabj.springboot.model.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
@EqualsAndHashCode
@Table(name = "user_info")
public class UserInfo implements Serializable {

    private static final long  serialVersionUID = 8399094400225924766L;
    @Id
    @Column(name = "u_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long               uId;

    @Basic
    @Column(name = "user_name")
    private String             userName;

    @Basic
    @Column(name = "login_name")
    private String             loginName;

    @Basic
    @Column(name = "pwd")
    private String             pwd;

    @Basic
    @ApiModelProperty(value = "有效标志")
    @Column(name = "disable_flag", nullable = false)
    private Long               disableFlag;
    /**
     * 创建时间
     */
    @Basic
    @ApiModelProperty(hidden = true)
    @Column(name = "gmt_create")
    private Date               gmtCreate;
    /**
     * 修改时间
     */
    @Basic
    @ApiModelProperty(hidden = true)
    @Column(name = "gmt_mod")
    private Date               gmtMod;

    /**
     * 登陆类型1、服务端、2、客户端
     */
    @Transient
    @ApiModelProperty(hidden = true)
    private String             loginType;

    /**
     * 用户资源
     */
    @Transient
    @ApiModelProperty(hidden = true)
    private List<SysResources> sysResourcesList;
}
