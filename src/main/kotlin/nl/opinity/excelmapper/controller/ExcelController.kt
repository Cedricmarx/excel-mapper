package nl.opinity.excelmapper.controller

import nl.opinity.excelmapper.service.ExcelService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/excel")
class ExcelController(private val excelService: ExcelService) {

    @PostMapping("/upload")
    fun uploadFile(@RequestParam("excel") excel: MultipartFile): ResponseEntity<Any> {
        return try {
            val json = excelService.convertXlsxToObject(excel)
            ResponseEntity.ok().body(json)
        } catch (e: Throwable) {
            e.printStackTrace()
            ResponseEntity.badRequest().body(e.message)
        }
    }
}