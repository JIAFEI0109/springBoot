package com.cqabj.springboot.model.entity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
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
@EqualsAndHashCode
@Table(name = "user_info")
public class UserInfo implements Serializable {

    private static final long  serialVersionUID = 8399094400225924766L;
    /**
     * 用户编号，系统中唯一标志
     */
    @Id
    @Column(name = "u_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "用户编号")
    private Long               uId;

    /**
     * 用户名称
     */
    @Basic
    @ApiModelProperty(value = "用户名称")
    @Column(name = "user_name")
    private String             userName;

    /**
     * 用户详细
     */
    @Basic
    @ApiModelProperty(value = "用户详细")
    @Column(name = "user_desc")
    private String             userDesc;

    /**
     * 用户密码
     */
    @Basic
    @ApiModelProperty(value = "用户密码")
    @Column(name = "login_pwd", nullable = false)
    private String             loginPwd;

    /**
     * 用户类型
     */
    @Basic
    @ApiModelProperty(value = "用户类型")
    @Column(name = "user_type")
    private String             userType;

    /**
     * 有效标志
     */
    @Basic
    @ApiModelProperty(value = "有效标志")
    @Column(name = "disable_flag", nullable = false)
    private Long               disableFlag;


    /**
     * 是否系统管理员 0 否  1是
     */
    @Basic
    @ApiModelProperty(value = "是否系统管理员")
    @Column(name = "is_sys", nullable = false)
    private Long               isSys;

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
     * 登录类型1、服务端2、客户端
     */
    @Transient
    @ApiModelProperty(hidden = true)
    private String loginType;

    /**
     * 用户资源
     */
    @Transient
    @ApiModelProperty(hidden = true)
    private List<SysResources> sysResourcesList;
}
