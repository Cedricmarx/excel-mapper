package nl.opinity.excelmapper.repository

import nl.opinity.excelmapper.model.ExcelObject
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ExcelRepository : JpaRepository<ExcelObject, Long>