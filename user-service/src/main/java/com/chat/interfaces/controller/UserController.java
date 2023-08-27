package com.chat.interfaces.controller;

import com.chat.model.view.UserView;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserController {

    @GetMapping("/v1/user/user")
    @PreAuthorize("hashRole('USER')")
    Mono<UserView> getUser();

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
//    @PreAuthorize("hashRole('USER')")
    @GetMapping("/v1/user/list")
    Flux<UserView> getUsers();
}
