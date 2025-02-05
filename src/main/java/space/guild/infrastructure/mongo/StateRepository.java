package space.guild.infrastructure.mongo;

import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.CrudRepository;
import space.guild.infrastructure.mongo.data.MongoState;

import java.util.Optional;

@MongoRepository
public interface StateRepository extends CrudRepository<MongoState, String> {
}
