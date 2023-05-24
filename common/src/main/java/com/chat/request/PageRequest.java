package com.chat.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageRequest {
    private int page;
    private String sort;
    private String direction;
}
