package com.chat.logging;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class LogbackTest {

    @Test
    public void testLogging(){
        log.info("Hello logstash. I'm in!!!!");
    }
}
