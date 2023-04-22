package com.chat.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class SearchRequest {
    @NotBlank(message = "0000")
    private String word;
}
