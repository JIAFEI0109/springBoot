package com.cqabj.springboot.model.entity;

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
 * @author fjia
 * @version V1.0 --2018/2/1-${time}
 */
@Data
@Entity
@ToString
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "access_key_info")
public class AccessKeyInfo implements Serializable {

    private static final long serialVersionUID = -245611806540572235L;

    @Id
    @Column(name = "rec_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long              recId;
    @Basic
    @Column(name = "access_key_id")
    private String            accessKeyId;
    @Basic
    @Column(name = "access_key_secret")
    private String            accessKeySecret;
    @Basic
    @Column(name = "create_time")
    private Date              createTime;
    @Basic
    @Column(name = "failure_time")
    private Date              failureTime;
    @Basic
    @Column(name = "access_key_desc")
    private String            accessKeyDesc;

}
