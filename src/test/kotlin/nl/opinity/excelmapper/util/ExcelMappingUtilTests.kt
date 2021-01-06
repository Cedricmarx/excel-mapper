package nl.opinity.excelmapper.util

import nl.opinity.excelmapper.exception.ExcelException
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.FileInputStream
import java.time.LocalDateTime

class ExcelMappingUtilTests {

    lateinit var workbook: Workbook

    @BeforeEach
    fun init() {
        workbook = WorkbookFactory.create(FileInputStream("src/test/resources/SampleData.xlsx"))
    }

    @Test
    fun mapHeadersShouldThrowExcelExceptionWhenNoHeadersFound() {
        workbook.createSheet("Test").createRow(0)
        val sheet = workbook.getSheet("Test")
        val iterator = sheet.iterator()

        val exception = assertThrows<ExcelException> { ExcelMappingUtil.mapHeaders(iterator, "SampleData", workbook) }

        assertEquals("No headers found in SampleData!", exception.message)
    }

    @Test
    fun mapHeadersShouldMapAllHeadersOfExcelSheet() {
        val sheet = workbook.getSheetAt(0)
        val iterator = sheet.iterator()

        val headers = ExcelMappingUtil.mapHeaders(iterator, "SampleData", workbook)

        assertEquals(headers, listOf("OrderDate", "Region", "Rep", "Item", "Units", "Unit Cost", "Total"))
    }

    @Test
    fun mapValuesToHeadersShouldMapCellValuesToHeaders() {
        val sheet = workbook.getSheetAt(0)
        val iterator = sheet.iterator()

        val headers = ExcelMappingUtil.mapHeaders(iterator, "SampleData", workbook)

        val response = ExcelMappingUtil.mapValuesToHeaders(iterator, headers, workbook)

        assertEquals(189.05, response[0].getValue("Total"))
        assertEquals(1.99, response[0].getValue("Unit Cost"))
        assertEquals(95.0, response[0].getValue("Units"))
        assertEquals("Pencil", response[0].getValue("Item"))
        assertEquals("Jones", response[0].getValue("Rep"))
        assertEquals("East", response[0].getValue("Region"))
        assertEquals(LocalDateTime.of(2019, 1, 6, 0, 0), response[0].getValue("OrderDate"))
    }
}