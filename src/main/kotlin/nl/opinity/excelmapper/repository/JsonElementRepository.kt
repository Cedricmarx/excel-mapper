package nl.opinity.excelmapper.repository

import com.google.gson.JsonElement
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface JsonElementRepository : MongoRepository<JsonElement, String>