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

    @Id
    @Column(name = "resource_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long              resourceId;

    @Basic
    @Column(name = "resource_type")
    private Long              resourceType;
    @Basic
    @Column(name = "resource_name", length = 100)
    private String            resourceName;

    @Basic
    @Column(name = "resource_desc", length = 200)
    private String            resourceDesc;

    @Basic
    @Column(name = "client_type")
    private Long              clientType;

    @Basic
    @Column(name = "resource_path", length = 200)
    private String            resourcePath;

    @Basic
    @Column(name = "interface_path", nullable = false, length = 200)
    private String            interfacePath;

    @Basic
    @Column(name = "disable_flag")
    private Long              disableFlag      = 1L;

    @Basic
    @Column(name = "sys_flag")
    private Long              sysFlag          = 0L;

    @Basic
    @Column(name = "gmt_create")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date              gmtCreate;

    @Basic
    @Column(name = "gmt_mod")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date              gmtMod;

}
