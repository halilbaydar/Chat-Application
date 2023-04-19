package com.chat.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CreateChatRoomRequest {
    @NotBlank(message = "username cannot be empty or null")
    private String username;
}
