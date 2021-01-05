package nl.opinity.excelmapper.service

import org.springframework.web.multipart.MultipartFile

interface ExcelService {
    fun convertXlsxToObject(file: MultipartFile): String
}