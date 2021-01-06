package nl.opinity.excelmapper.service

import org.apache.logging.log4j.LogManager
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Service

@Service
class MongoServiceImpl(val mongoTemplate: MongoTemplate) : MongoService {

    private val log = LogManager.getLogger(javaClass)

    override fun saveAll(collection: Collection<Any>, collectionName: String) {
        collection.forEach { map -> mongoTemplate.save(map, collectionName) }
        log.debug("Saved collection to: {$collectionName}")
    }

    override fun dropCollectionIfExists(collectionName: String) {
        if (mongoTemplate.collectionExists(collectionName)) {
            mongoTemplate.dropCollection(collectionName)
            log.debug("Dropped already existing collection: {$collectionName}")
        }
    }
}
