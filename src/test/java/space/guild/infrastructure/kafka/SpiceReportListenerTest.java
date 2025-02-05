package space.guild.infrastructure.kafka;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import space.guild.infrastructure.kafka.data.KafkaSpiceReport;
import space.guild.testing.KafkaMongoTest;
import space.guild.testing.KafkaSpiceReportTestingProducer;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SpiceReportListenerTest extends KafkaMongoTest {

    @Inject
    KafkaSpiceReportTestingProducer testingProducer;

    @Test
    void onEvent(){
        //given
        String eventUuid = UUID.randomUUID().toString();
        var kafkaSpiceReport = new KafkaSpiceReport(
                Instant.now().toEpochMilli(),
                eventUuid,
                "id",
                "reportName",
                "reportContent"
        );
        //when
        testingProducer.emitEvent(kafkaSpiceReport).block();
        //then
        safeWait();
        assertEquals(eventUuid,SpiceReportListener.getLastUuid());
    }

    private static void safeWait() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public MongoSetup mongoSetup() {
        return null;
    }
}