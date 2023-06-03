package com.chat.unit.controller;

import com.chat.common.AbstractIT;
import com.chat.config.CorsConfig;
import com.chat.interfaces.controller.ChatController;
import com.chat.interfaces.service.ChatService;
import com.chat.model.entity.ChatEntity;
import com.chat.model.request.CreateChatRoomRequest;
import com.chat.rabbit.RabbitUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(value = "test")
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(value = {MockitoExtension.class})
@WebMvcTest(controllers = {ChatController.class})
class ChatControllerImplTest extends AbstractIT {

    @MockBean
    private ChatService chatService;

    @MockBean
    private RabbitUserService rabbitUserService;

    @MockBean
    private CorsConfig config;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        Mockito.when(rabbitUserService.receiveUserFromUserService(anyString())).thenReturn(user1);
    }

    @Test
    void createChatRoom() throws Exception {
        Mockito.when(chatService.createChatRoom(any())).thenReturn(chat);

        CreateChatRoomRequest createChatRoomRequest = new CreateChatRoomRequest();
        createChatRoomRequest.setUsername("username-1");
        String content = objectWriter.writeValueAsString(createChatRoomRequest);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.post("http://localhost:91/v1/user/chat/room")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content);

        this.mockMvc
                .perform(mockHttpServletRequestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.type", is(ChatEntity.ChatType.SINGLE.name())))
                .andExpect(jsonPath("$.id", is(String.valueOf(id))));
    }

    @Test
    void getChatsByPagination() {
    }

    @Test
    void getChatMessagesByPagination() {
    }

}