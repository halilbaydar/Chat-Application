package com.chat.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class AbstractEntity<ID extends Serializable> {

    @Column("created_at")
    @CreatedDate
    private final Long createdAt = new Date().getTime();
    @Column("updated_at")
    @LastModifiedDate
    private final Long updatedAt = new Date().getTime();
    @Id
    private ID id;
    @Column("deleted_at")
    private Long deletedAt;

    public boolean isEmpty() {
        return this.getId() == null;
    }
}
