package com.chat.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_permission")
public class UserPermission extends AbstractEntity<BigInteger> implements Serializable {

    @Column
    private Integer userId;

    @Column
    private BigInteger resourceId;

    @Column
    private String permissionType;
}
