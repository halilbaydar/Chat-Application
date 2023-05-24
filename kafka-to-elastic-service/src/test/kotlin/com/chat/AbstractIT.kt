package com.chat

import io.confluent.kafka.serializers.KafkaAvroDeserializer
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.test.EmbeddedKafkaBroker
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.kafka.test.utils.KafkaTestUtils
import reactor.kafka.receiver.KafkaReceiver
import reactor.kafka.receiver.ReceiverOptions
import java.util.function.UnaryOperator

@SpringBootTest
@EmbeddedKafka(
    topics = ["user-to-elastic"],
    partitions = 1,
    ports = [19092],
)
abstract class AbstractIT {
    @Autowired
    private lateinit var embeddedKafkaBroker: EmbeddedKafkaBroker

    protected fun <V> createReceiver(topics: List<String>): KafkaReceiver<String, V> {
        val valueDeserializer = KafkaAvroDeserializer() as Deserializer<V>
        return createReceiver { options ->
            options.withKeyDeserializer(StringDeserializer())
                .withValueDeserializer(valueDeserializer)
                .subscription(topics)
        }
    }

    protected fun <K, V> createReceiver(builder: (ops: ReceiverOptions<K, V>) -> ReceiverOptions<K, V>):
            KafkaReceiver<K, V> {
        val props = KafkaTestUtils.consumerProps("test-group", "true", embeddedKafkaBroker)
        val options = ReceiverOptions.create<K, V>(props)
        return KafkaReceiver.create(builder(options))
    }
}