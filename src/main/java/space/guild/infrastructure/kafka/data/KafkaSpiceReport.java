package space.guild.infrastructure.kafka.data;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record KafkaSpiceReport(
        Long kafkaEventProducedAt,
        String kafkaEventUuid,
        String id,
        String reportName,
        String reportContent) {
}
