package space.guild.testing;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.Topic;
import reactor.core.publisher.Mono;
import space.guild.infrastructure.kafka.data.KafkaSpiceReport;

@KafkaClient
public interface KafkaSpiceReportTestingProducer {

    @Topic("spicereport")
    Mono<KafkaSpiceReport> emitEvent(KafkaSpiceReport report);

}
