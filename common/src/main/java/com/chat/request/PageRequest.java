package com.chat.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public class PageRequest {
    private int page;
    private String sort;
    private Sort.Direction direction;
}
