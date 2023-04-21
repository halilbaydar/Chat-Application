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
    public ResponseEntity<ChatServiceFallbackModel> chatServiceFallback() {
        LOG.info("Returning fallback result for chat service! on port {}", port);
        return ResponseEntity.ok(ChatServiceFallbackModel.builder()
                .fallbackMessage("Fallback result for chat service!")
                .build());
    }

    @PostMapping("/user-fallback")
    public ResponseEntity<ChatServiceFallbackModel> userServiceFallback() {
        LOG.info("Returning fallback result for user service! on port {}", port);
        return ResponseEntity.ok(ChatServiceFallbackModel.builder()
                .fallbackMessage("Fallback result for user-service!")
                .build());
    }

    @PostMapping("/search-fallback")
    public ResponseEntity<ChatServiceFallbackModel> searchServiceFallback() {
        LOG.info("Returning fallback result for search service! on port {}", port);
        return ResponseEntity.ok(ChatServiceFallbackModel.builder()
                .fallbackMessage("Fallback result for search service!")
                .build());
    }

    @PostMapping("/auth-fallback")
    public ResponseEntity<ChatServiceFallbackModel> authServiceFallback() {
        LOG.info("Returning fallback result for auth service! on port {}", port);
        return ResponseEntity.ok(ChatServiceFallbackModel.builder()
                .fallbackMessage("Fallback result for auth service!")
                .build());
    }
}
