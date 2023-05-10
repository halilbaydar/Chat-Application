package com.chat.model.request;

import com.chat.request.PageRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class SearchRequest extends PageRequest {
    @NotBlank(message = "0000")
    private String keyword;
}
