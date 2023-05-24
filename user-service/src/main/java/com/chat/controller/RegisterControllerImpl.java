package com.chat.controller;
//
//import com.chat.interfaces.controller.RegisterController;
//import com.chat.interfaces.service.RegisterService;
//import com.chat.model.request.RegisterRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.RestController;
//import reactor.core.publisher.Mono;
//
//@RestController
//@RequiredArgsConstructor
//public class RegisterControllerImpl implements RegisterController {
//    private final RegisterService registerService;
//
//    @Override
//    public RegisterService getService() {
//        return registerService;
//    }
//
//    @Override
//    public Mono<String> register(Mono<RegisterRequest> registerRequest) {
//        return getService().register(registerRequest);
//    }
//}
