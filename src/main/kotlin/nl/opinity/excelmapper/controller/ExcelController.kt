package nl.opinity.excelmapper.controller

import nl.opinity.excelmapper.service.ExcelService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile


@RestController
class ExcelController(private val excelService: ExcelService) {

    @PostMapping("/uploadFile")
    fun uploadFile(@RequestParam("file") file: MultipartFile): ResponseEntity<Any> {
        return try {
            val jsonObjects = excelService.convertXlsxToObject(file)
            ResponseEntity.ok().body(jsonObjects)
        } catch (e: Throwable) {
            e.printStackTrace()
            ResponseEntity.badRequest().body(e.message)
        }
    }
}