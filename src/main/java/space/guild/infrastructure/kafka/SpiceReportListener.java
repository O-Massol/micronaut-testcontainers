package space.guild.infrastructure.kafka;

import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import space.guild.infrastructure.kafka.data.KafkaSpiceReport;

import java.util.concurrent.atomic.AtomicReference;

@KafkaListener
public class SpiceReportListener {

    private static AtomicReference<String> lastUuid =  new AtomicReference<>("NADA");

    @Topic("spicereport")
    public void onEvent(KafkaSpiceReport kafkaSpiceReport) {
        lastUuid.set(kafkaSpiceReport.kafkaEventUuid());
        System.out.println("onEvent :" + kafkaSpiceReport);
    }

    public static String getLastUuid(){
        return lastUuid.get();
    }
}