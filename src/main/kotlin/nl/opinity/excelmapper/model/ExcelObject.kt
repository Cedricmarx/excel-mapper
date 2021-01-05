package nl.opinity.excelmapper.model

import javax.persistence.*

@Entity
data class ExcelObject(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = 0,
        @Column(columnDefinition = "TEXT")
        val jsonObject: String,
        val fileName: String
)
