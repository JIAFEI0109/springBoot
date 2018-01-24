package com.cqabj.springboot.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author fjia
 * @version V1.0 --2018/1/22-${time}
 */
@Data
//@Entity
@ToString
@NoArgsConstructor
@EqualsAndHashCode
//@Table(name = "")
public class First implements Serializable {

    private static final long serialVersionUID = 4376561351934082896L;

    private Long              id;

    private String            name;
}
