package nl.opinity.excelmapper.service

import nl.opinity.excelmapper.model.ExcelObject
import org.springframework.web.multipart.MultipartFile

interface ExcelService {
    fun convertXlsxToObject(file: MultipartFile): ExcelObject
}