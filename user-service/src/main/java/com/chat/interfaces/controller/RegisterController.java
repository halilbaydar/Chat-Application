package com.chat.interfaces.controller;

import com.chat.interfaces.service.RegisterService;
import com.chat.model.request.RegisterRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@RequestMapping(path = "/v1/register")
public interface RegisterController extends ParentController<RegisterService> {
    //    @Operation(summary = "Register User.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Successful response.", content = {
//                    @Content(mediaType = "application/vnd.api.v1+json",
//                            schema = @Schema(implementation = String.class)
//                    )
//            }),
//            @ApiResponse(responseCode = "400", description = "Bad request."),
//            @ApiResponse(responseCode = "500", description = "Internal server error.")
//    })
    @PostMapping()
    Mono<String> register(@RequestBody Mono<RegisterRequest> registerRequest);
}
