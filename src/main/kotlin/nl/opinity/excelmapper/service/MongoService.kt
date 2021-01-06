package nl.opinity.excelmapper.service

interface MongoService {

    fun saveAll(collection: Collection<Any>, collectionName: String)

    fun dropCollectionIfExists(collectionName: String)
}
