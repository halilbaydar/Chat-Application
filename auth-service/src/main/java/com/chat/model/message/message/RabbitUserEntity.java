package com.chat.model.message.message;

import lombok.*;

import java.io.Serializable;
import java.math.BigInteger;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
