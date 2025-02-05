package space.guild.infrastructure.mongo.data;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;

import java.time.ZonedDateTime;

@MappedEntity("state")
public record MongoState(
        @Id @GeneratedValue String id,
        String reportId,
        String eventId,
        ZonedDateTime eventDatetime){
}
