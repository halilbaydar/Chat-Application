package com.chat.kafka.producer;

public interface KafkaProducer<T, R> {
    R send(String traceId, T t);
}
