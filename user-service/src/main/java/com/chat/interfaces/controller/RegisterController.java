package com.chat.interfaces.controller;

import com.chat.interfaces.service.RegisterService;
import com.chat.model.request.RegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.PostMapping;
import reactor.core.publisher.Mono;

public interface RegisterController extends ParentController<RegisterService> {
    @Operation(summary = "Register User.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response.", content = {
                    @Content(mediaType = "application/vnd.api.v1+json",
                            schema = @Schema(implementation = String.class)
                    )
            }),
            @ApiResponse(responseCode = "400", description = "Bad request."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    @PostMapping("/register")
    Mono<String> register(Mono<RegisterRequest> registerRequest);
}
