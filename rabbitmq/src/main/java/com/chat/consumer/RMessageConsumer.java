package com.chat.consumer;

import java.io.Serializable;

public interface RMessageConsumer<M extends Serializable, R, E extends Throwable> {
    R consume(M m) throws E;
}
