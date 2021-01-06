package nl.opinity.excelmapper.service

import org.springframework.web.multipart.MultipartFile

interface ExcelService {

    fun convertToObjectAndStore(file: MultipartFile): List<Map<Any, Any>>
}
