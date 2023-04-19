package com.chat.controller;

import com.chat.model.ChatServiceFallbackModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    private static final Logger LOG = LoggerFactory.getLogger(FallbackController.class);

    @Value("${server.port}")
    private String port;

    @PostMapping("/chat-fallback")
    public ResponseEntity<ChatServiceFallbackModel> myNameServiceFallback() {
        LOG.info("Returning fallback result for myname-service! on port {}", port);
        return ResponseEntity.ok(ChatServiceFallbackModel.builder()
                .fallbackMessage("Fallback result for my-name service!")
                .build());
    }
}
