package com.chat.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

@Getter
@Setter
public class PageNumberRequest {
    @Min(value = 0, message = "@@@@@@@")
    private int pageNumber;
}
