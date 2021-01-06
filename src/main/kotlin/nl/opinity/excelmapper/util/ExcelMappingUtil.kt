package nl.opinity.excelmapper.util

import nl.opinity.excelmapper.exception.ExcelException
import org.apache.logging.log4j.LogManager
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Workbook

object ExcelMappingUtil {

    private val log = LogManager.getLogger(javaClass)

    fun mapHeaders(iterator: MutableIterator<Row>, fileName: String, workbook: Workbook): List<Any> {
        log.info("Finding headers in $fileName..")
        val headers: MutableList<Any> = ArrayList()
        val row = iterator.next()
        val cellIterator = row.cellIterator()

        if (!cellIterator.hasNext()) throw ExcelException("No headers found in $fileName!")

        while (cellIterator.hasNext()) {
            val cell = cellIterator.next()
            headers.add(ExcelCellValueUtil.getCellValue(cell, true, workbook))
        }
        log.debug("${headers.size} headers found in excel sheet: $headers")
        return headers
    }

    fun mapValuesToHeaders(iterator: MutableIterator<Row>, headers: List<Any>, workbook: Workbook): List<Map<Any, Any>> {
        val list: MutableList<Map<Any, Any>> = ArrayList()
        log.info("Mapping cell values to headers..")

        while (iterator.hasNext()) {
            val tempMap: MutableMap<Any, Any> = HashMap()
            val nextRow = iterator.next()
            val cellIterator = nextRow.cellIterator()

            while (cellIterator.hasNext()) {
                val cell = cellIterator.next()
                tempMap[headers[cell.columnIndex]] = ExcelCellValueUtil.getCellValue(cell, false, workbook)
            }
            list.add(tempMap)
        }
        return list
    }
}
