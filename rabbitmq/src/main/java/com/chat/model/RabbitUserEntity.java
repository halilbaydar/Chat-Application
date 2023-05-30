package com.chat.model;

import java.io.Serializable;
import java.math.BigInteger;

public record RabbitUserEntity(BigInteger id,
                               String username,
                               String name,
                               String role,
                               String status,
                               String password,
                               Long createdAt,
                               Long deletedAt,
                               Long updatedAt
) implements Serializable {

}
