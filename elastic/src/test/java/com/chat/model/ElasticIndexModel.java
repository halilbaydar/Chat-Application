package com.chat.model;

import java.io.Serializable;

public interface ElasticIndexModel<ID extends Serializable> {
    ID getId();
}
