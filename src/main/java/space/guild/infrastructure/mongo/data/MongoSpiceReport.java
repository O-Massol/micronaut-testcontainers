package space.guild.infrastructure.mongo.data;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;

@MappedEntity("spice-report-10190")
public record MongoSpiceReport (
        @Id @GeneratedValue String id,
        String reportName,
        String reportContent){
}
