package nl.opinity.excelmapper.service

import com.google.gson.GsonBuilder
import lombok.extern.log4j.Log4j2
import nl.opinity.excelmapper.exception.ExcelException
import nl.opinity.excelmapper.model.ExcelObject
import nl.opinity.excelmapper.repository.ExcelRepository
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.DateUtil
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile

@Service
@Log4j2
class ExcelServiceImpl(val excelRepository: ExcelRepository) : ExcelService {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun convertXlsxToObject(file: MultipartFile): String {
        val fileName: String = StringUtils.cleanPath(file.originalFilename.orEmpty())
        val fileExtension = fileName.split('.')[1].toLowerCase()

        log.info("Validating file..")
        if ("xlsx" != fileExtension) {
            throw ExcelException("Not a valid Excel extension: {${fileExtension}}")
        }

        if (!fileName.matches(Regex("^[\\w\\-. ]+$"))) {
            throw ExcelException("Filename contains invalid path sequence: {$fileName}")
        }

        log.debug("File $fileName is a valid file!")
        log.info("Converting to object..")

        val workbook = XSSFWorkbook(file.inputStream)
        val sheet = workbook.getSheetAt(1)
        val iterator = sheet.iterator()

        val headers = getHeadersFromSheet(iterator)
        val map = mapHeadersToSheetValues(iterator, headers)

        val gson = GsonBuilder().setPrettyPrinting().create()
        val jsonString = gson.toJson(map)
        excelRepository.save(ExcelObject(jsonObjects = jsonString, fileName = fileName))
        return jsonString
    }

    fun getHeadersFromSheet(iterator: MutableIterator<Row>): MutableList<String> {
        val headers: MutableList<String> = ArrayList()
        val row = iterator.next()
        val cellIterator = row.cellIterator()

        while (cellIterator.hasNext()) {
            val cell = cellIterator.next()
            headers.add(cell.stringCellValue)
        }
        return headers
    }

    fun mapHeadersToSheetValues(iterator: MutableIterator<Row>, headers: MutableList<String>): MutableMap<Long, MutableMap<String, Any>> {
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
            map[nextRow.rowNum.toLong()] = tempMap
        }
        return map
    }

    fun getNumericCellValue(cell: Cell): Any {
        if (DateUtil.isCellDateFormatted(cell)) {
            return cell.dateCellValue
        }
        return cell.numericCellValue
    }
}
