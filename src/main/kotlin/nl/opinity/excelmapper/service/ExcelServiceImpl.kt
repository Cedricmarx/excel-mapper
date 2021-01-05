package nl.opinity.excelmapper.service

import com.google.gson.GsonBuilder
import nl.opinity.excelmapper.exception.ExcelException
import nl.opinity.excelmapper.model.ExcelObject
import nl.opinity.excelmapper.repository.ExcelRepository
import org.apache.logging.log4j.LogManager
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.DateUtil
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile

@Service
class ExcelServiceImpl(val excelRepository: ExcelRepository) : ExcelService {

    private val log = LogManager.getLogger(javaClass)

    override fun convertXlsxToObject(file: MultipartFile): String {
        val fileName: String = StringUtils.cleanPath(file.originalFilename.orEmpty())

        log.info("Validating excel file..")
        if ("xlsx" != fileName.split('.')[1].toLowerCase())
            throw ExcelException("$fileName doesn't have a valid excel extension!")
        if (!fileName.matches(Regex("^[\\w\\-. ]+$")))
            throw ExcelException("$fileName contains invalid path sequence!")
        log.debug("$fileName is a valid excel file!")

        log.info("Reading $fileName..")
        val workbook = XSSFWorkbook(file.inputStream)
        val sheet = workbook.getSheetAt(1)
        val iterator = sheet.iterator()

        val headers = getHeadersFromSheet(iterator, fileName)
        val map = mapHeadersToSheetValues(iterator, headers)
        return convertToJsonAndStore(map, fileName)
    }

    private fun convertToJsonAndStore(map: MutableMap<Long, MutableMap<String, Any>>, fileName: String): String {
        log.debug("Converting mapped objects to JSON..")
        val gson = GsonBuilder().setPrettyPrinting().create()
        val jsonString = gson.toJson(map)
        val excelObject = excelRepository.save(ExcelObject(jsonObjects = jsonString, fileName = fileName))
        log.debug("Returned mapped excel object: $excelObject")
        return jsonString
    }

    private fun getHeadersFromSheet(iterator: MutableIterator<Row>, fileName: String): MutableList<String> {
        val headers: MutableList<String> = ArrayList()
        val row = iterator.next()
        val cellIterator = row.cellIterator()

        if (!cellIterator.hasNext()) throw ExcelException("No headers found in $fileName!")

        while (cellIterator.hasNext()) {
            val cell = cellIterator.next()
            headers.add(cell.stringCellValue)
        }
        log.debug("${headers.size} headers found in excel sheet: $headers")
        return headers
    }

    private fun mapHeadersToSheetValues(iterator: MutableIterator<Row>, headers: MutableList<String>): MutableMap<Long, MutableMap<String, Any>> {
        log.info("Mapping cell values to headers..")
        val map: MutableMap<Long, MutableMap<String, Any>> = HashMap()

        while (iterator.hasNext()) {
            val tempMap: MutableMap<String, Any> = HashMap()
            val nextRow = iterator.next()
            val cellIterator = nextRow.cellIterator()

            while (cellIterator.hasNext()) {
                val cell = cellIterator.next()
                when (cell.cellType) {
                    CellType.STRING -> tempMap[headers[cell.columnIndex]] = cell.stringCellValue
                    CellType.BOOLEAN -> tempMap[headers[cell.columnIndex]] = cell.booleanCellValue
                    CellType.NUMERIC -> tempMap[headers[cell.columnIndex]] = getNumericCellValue(cell)
                    else -> println("CELL NOT SUPPORTED")
                }
            }
            log.debug("Object mapped at row ${nextRow.rowNum}: $tempMap")
            map[nextRow.rowNum.toLong()] = tempMap
        }
        return map
    }

    private fun getNumericCellValue(cell: Cell): Any {
        if (DateUtil.isCellDateFormatted(cell)) {
            return cell.dateCellValue
        }
        return cell.numericCellValue
    }
}
