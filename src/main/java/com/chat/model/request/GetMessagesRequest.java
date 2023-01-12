package com.chat.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

@Getter
@Setter
public class GetMessagesRequest extends ByIdRequest {
    @Min(value = 0)
    private int pageNumber;

    public GetMessagesRequest(int pageNumber, String id) {
        super(id);
        this.pageNumber = pageNumber;
    }
}
