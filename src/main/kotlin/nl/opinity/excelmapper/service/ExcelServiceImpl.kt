package nl.opinity.excelmapper.service

import nl.opinity.excelmapper.exception.ExcelException
import nl.opinity.excelmapper.util.ExcelMappingUtil
import org.apache.logging.log4j.LogManager
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile

@Service
class ExcelServiceImpl(val mongoService: MongoService) : ExcelService {

    private val log = LogManager.getLogger(javaClass)

    override fun convertToObjectAndStore(file: MultipartFile): List<Map<Any, Any>> {
        log.info("Starting Excel document conversion..")
        val supportedFileExtensions = arrayOf("xlsx", "xls")

        val completeFileName: String = StringUtils.cleanPath(file.originalFilename.orEmpty())
        val completeFileNameList = completeFileName.split('.')
        val fileName = completeFileNameList[0]
        val fileExtension = completeFileNameList[1].toLowerCase()

        if (!supportedFileExtensions.contains(fileExtension))
            throw ExcelException("$fileName doesn't have a valid excel extension {$fileExtension}!")
        if (!completeFileName.matches(Regex("^[\\w\\-. ]+$")))
            throw ExcelException("$completeFileName contains invalid path sequence!")
        log.debug("$completeFileName is a valid excel file!")

        mongoService.dropCollectionIfExists(fileName)

        val workbook = WorkbookFactory.create(file.inputStream)
        val sheet = workbook.getSheetAt(0)
        val iterator = sheet.iterator()

        val headers = ExcelMappingUtil.mapHeaders(iterator, completeFileName, workbook)
        val mappings = ExcelMappingUtil.mapValuesToHeaders(iterator, headers, workbook)
        mongoService.saveAll(mappings, fileName)
        return mappings
    }
}
