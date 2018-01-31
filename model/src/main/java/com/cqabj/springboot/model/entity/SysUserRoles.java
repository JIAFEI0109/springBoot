package com.cqabj.springboot.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 用户角色关系表
 * @author fjia
 * @version V1.0 --2018/1/31-${time}
 */
@Data
@ToString
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "sys_user_roles")
public class SysUserRoles implements Serializable {
    private static final long serialVersionUID = 5589914567824389770L;

    @Id
    @Column(name = "rec_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long              recId;

    @Basic
    @Column(name = "user_id", nullable = false)
    private Long              userId;

    @Basic
    @Column(name = "role_id", nullable = false)
    private Long              roleId;
}
