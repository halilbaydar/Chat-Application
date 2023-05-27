package com.chat.model.message.message;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigInteger;

@Builder
@Getter
@Setter
public class RabbitUserEntity implements Serializable {
    private BigInteger id;
    private Long createdAt;
    private Long deletedAt;
    private Long updatedAt;
    private String username;
    private String name;
    private String role;
    private String status;
    private String password;
}
