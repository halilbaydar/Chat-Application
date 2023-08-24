package com.chat.model.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
public abstract class BaseEntity<ID extends Serializable, T extends BaseEntity> {

    @Id
    @Column
    private ID id;

    @CreatedDate
    @Column
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column
    private LocalDateTime updatedAt;
}
