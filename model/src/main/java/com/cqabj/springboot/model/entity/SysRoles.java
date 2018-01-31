package com.cqabj.springboot.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
import java.io.Serializable;
import java.util.Date;

/**
 * 系统角色表
 * @author fjia
 * @version V1.0 --2018/1/31-${time}
 */
@Data
@Entity
@ToString
@DynamicInsert
@DynamicUpdate
@EqualsAndHashCode
@NoArgsConstructor
@Table(name = "sys_roles")
public class SysRoles implements Serializable {

    private static final long serialVersionUID = -7988484633800550140L;

    @Id
    @Column(name = "role_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long              roleId;

    @Basic
    @Column(name = "role_name", length = 100)
    private String            roleName;

    @Basic
    @Column(name = "role_desc", length = 200)
    private String            roleDesc;

    @Basic
    @Column(name = "disable_flag")
    private Long              disableFlag      = 1L;

    @Basic
    @Column(name = "gmt_create")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date              gmtCreate;

    @Basic
    @Column(name = "gmt_mod")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date              gmtMod;
}
