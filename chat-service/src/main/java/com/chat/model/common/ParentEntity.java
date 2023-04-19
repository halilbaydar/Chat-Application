package com.chat.model.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public abstract class ParentEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field
    @CreatedDate
    private Date createdDate;
}
