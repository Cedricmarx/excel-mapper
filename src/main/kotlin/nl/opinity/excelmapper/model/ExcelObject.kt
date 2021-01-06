package nl.opinity.excelmapper.model

import org.springframework.data.mongodb.core.mapping.Document
import javax.persistence.*

@Document
data class ExcelObject(
        @Id
        val id: String = "",
        @Column(columnDefinition = "TEXT")
        val jsonObject: String,
        val fileName: String
)
