package nl.opinity.excelmapper.service

import com.google.gson.JsonElement
import nl.opinity.excelmapper.model.ExcelObject
import org.springframework.web.multipart.MultipartFile

interface ExcelService {
    fun convertXlsxToObject(file: MultipartFile): MutableList<JsonElement>
}