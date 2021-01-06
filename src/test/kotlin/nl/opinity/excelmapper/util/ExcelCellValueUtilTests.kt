package nl.opinity.excelmapper.util

import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.FileInputStream
import java.time.LocalDateTime
import java.util.*
import kotlin.random.Random


class ExcelCellValueUtilTests {

    lateinit var workbook: Workbook
    lateinit var cell: Cell

    @BeforeEach
    fun init() {
        workbook = WorkbookFactory.create(FileInputStream("src/test/resources/SampleData.xlsx"))
        val row = workbook.getSheetAt(0).createRow(Random.nextInt(1000, 2000))
        cell = row.createCell(0)
    }

    @Test
    fun getCellValueShouldReturnStringIfCellTypeIsString() {
        cell.setCellValue(UUID.randomUUID().toString())

        val cellValue = ExcelCellValueUtil.getCellValue(cell, false, workbook)

        Assertions.assertTrue(cellValue is String)
    }

    @Test
    fun getCellValueShouldReturnBooleanIfCellTypeIsBoolean() {
        cell.setCellValue(Random.nextBoolean())

        val cellValue = ExcelCellValueUtil.getCellValue(cell, false, workbook)

        Assertions.assertTrue(cellValue is Boolean)
    }

    @Test
    fun getCellValueShouldReturnLocalDateIfCellTypeIsNumericAndDateFormatted() {
        cell.setCellValue(LocalDateTime.now())

        val style = workbook.createCellStyle()
        style.dataFormat = workbook.creationHelper.createDataFormat().getFormat("dd-mm-yyyy")
        cell.cellStyle = style

        val cellValue = ExcelCellValueUtil.getCellValue(cell, false, workbook)

        Assertions.assertTrue(cellValue is LocalDateTime)
    }

    @Test
    fun getCellValueShouldReturnNumericIfCellTypeIsNumericAndNotIsHeader() {
        cell.setCellValue(Random.nextDouble())

        val cellValue = ExcelCellValueUtil.getCellValue(cell, false, workbook)

        Assertions.assertTrue(cellValue is Double)
    }

    @Test
    fun getCellValueShouldReturnIntIfCellTypeIsNumericAndIsHeader() {
        cell.setCellValue(Random.nextDouble())

        val cellValue = ExcelCellValueUtil.getCellValue(cell, true, workbook)

        Assertions.assertTrue(cellValue is Int)
    }
}