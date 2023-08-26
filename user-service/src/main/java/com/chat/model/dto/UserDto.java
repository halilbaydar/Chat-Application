package com.chat.model.dto;

import com.chat.model.view.UserView;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserDto {
    private String username;
    private String role;
    private String status;
    private String password;

    public static UserDto fromView(UserView userView) {
        return UserDto.builder()
                .username(userView.getUsername())
                .password(userView.getPassword())
                .role(userView.getRole())
                .build();
    }
}
