package nl.opinity.excelmapper.util

import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.FileInputStream
import java.time.LocalDateTime


class ExcelCellValueUtilTests {

    lateinit var workbook: Workbook
    lateinit var cell: Cell

    @BeforeEach
    fun init() {
        workbook = WorkbookFactory.create(FileInputStream("src/test/resources/SampleData.xlsx"))
        val row = workbook.getSheetAt(0).createRow(999)
        cell = row.createCell(0)
    }

    @Test
    fun getCellValueShouldReturnStringIfCellTypeIsString() {
        cell.setCellValue("String")

        val cellValue = ExcelCellValueUtil.getCellValue(cell, false, workbook)

        Assertions.assertTrue(cellValue is String)
    }

    @Test
    fun getCellValueShouldReturnBooleanIfCellTypeIsBoolean() {
        cell.setCellValue(true)

        val cellValue = ExcelCellValueUtil.getCellValue(cell, false, workbook)

        Assertions.assertTrue(cellValue is Boolean)
    }

    @Test
    fun getCellValueShouldReturnLocalDateIfCellTypeIsNumericAndDateFormatted() {
        cell.setCellValue(LocalDateTime.of(2021, 1, 6, 0, 0))

        val style = workbook.createCellStyle()
        style.dataFormat = workbook.creationHelper.createDataFormat().getFormat("dd-mm-yyyy")
        cell.cellStyle = style

        val cellValue = ExcelCellValueUtil.getCellValue(cell, false, workbook)

        Assertions.assertTrue(cellValue is LocalDateTime)
    }

    @Test
    fun getCellValueShouldReturnNumericIfCellTypeIsNumericAndNotIsHeader() {
        cell.setCellValue(1.0)

        val cellValue = ExcelCellValueUtil.getCellValue(cell, false, workbook)

        Assertions.assertTrue(cellValue is Double)
    }

    @Test
    fun getCellValueShouldReturnIntIfCellTypeIsNumericAndIsHeader() {
        cell.setCellValue(1.0)

        val cellValue = ExcelCellValueUtil.getCellValue(cell, true, workbook)

        Assertions.assertTrue(cellValue is Int)
    }
}