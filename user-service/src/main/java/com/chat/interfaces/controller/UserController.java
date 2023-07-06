package com.chat.interfaces.controller;

import com.chat.model.entity.UserResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequestMapping(path = "/v1")
public interface UserController {

    @GetMapping("/user")
    @PreAuthorize("hashRole('USER') && hasPermission(returnObject, 'READ_USER')")
    Mono<UserResponse> getUser();

    //    @Operation(summary = "Get All users.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Successful response.", content = {
//                    @Content(mediaType = "application/vnd.api.v1+json",
//                            schema = @Schema(implementation = UserResponse.class)
//                    )
//            }),
//            @ApiResponse(responseCode = "404", description = "Bad request."),
//            @ApiResponse(responseCode = "500", description = "Internal server error.")
//    })
    @GetMapping("/user/users")
    Flux<UserResponse> getUsers();
}
