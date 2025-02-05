package space.guild.infrastructure.mongo;

import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.CrudRepository;
import space.guild.infrastructure.mongo.data.MongoSpiceReport;

@MongoRepository
public interface SpiceReportRepository extends CrudRepository<MongoSpiceReport, String> {
}
