package nl.opinity.excelmapper

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class])
class ExcelMapperApplication

fun main(args: Array<String>) {
    runApplication<ExcelMapperApplication>(*args)
}
