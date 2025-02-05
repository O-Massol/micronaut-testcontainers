package space.guild.infrastructure.mongo;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import space.guild.infrastructure.mongo.data.MongoSpiceReport;
import space.guild.infrastructure.mongo.data.MongoState;
import space.guild.testing.KafkaMongoTest;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StateRepositoryTest extends KafkaMongoTest {

    @Inject
    private StateRepository repository;


    @Test
    void saveAndFindAll() {
        //given
        var reportId = "1";
        var eventId = UUID.randomUUID().toString();
        var eventDateTime = ZonedDateTime.of(2025,2,16,12,0,0,0, ZoneId.of("Europe/Paris"));
        MongoState mongoState = new MongoState(null, reportId, eventId, eventDateTime);
        //when
        repository.save(mongoState);
        //then
        var reports = repository.findAll().stream().filter(r -> eventId.equals(r.eventId())).toList();
        assertEquals(1, reports.size());
    }


    @Override
    public MongoSetup mongoSetup() {
        return new MongoSetup("SPACEGUILD", List.of("spice-report-10190"));
    }
}
