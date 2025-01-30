package space.guild.infrastructure.mongo.data;

import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.CrudRepository;

@MongoRepository
public interface SpiceReportRepository extends CrudRepository<MongoSpiceReport, String> {
}
