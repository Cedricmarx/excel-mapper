package nl.opinity.excelmapper.repository

import nl.opinity.excelmapper.model.ExcelObject
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ExcelRepository : MongoRepository<ExcelObject, String>