package org.trade.option.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.trade.option.entity.OptionEntity;

import java.util.UUID;

public interface OptionEntityRepository extends MongoRepository<OptionEntity, UUID> {
}
