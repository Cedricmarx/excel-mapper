package nl.opinity.excelmapper.util

import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.DateUtil
import org.apache.poi.ss.usermodel.Workbook

object ExcelCellValueUtil {

    fun getCellValue(cell: Cell, isHeader: Boolean, workbook: Workbook): Any {
        val formulaEvaluator = workbook.creationHelper.createFormulaEvaluator()
        val cellValue = formulaEvaluator.evaluate(cell)

        return when (cellValue.cellType) {
            CellType.STRING -> cell.stringCellValue
            CellType.BOOLEAN -> cell.booleanCellValue
            CellType.NUMERIC -> getNumericCellValue(cell, isHeader)
            else -> ""
        }
    }

    private fun getNumericCellValue(cell: Cell, isHeader: Boolean): Any {
        if (DateUtil.isCellDateFormatted(cell)) {
            return cell.localDateTimeCellValue
        }
        return if (isHeader) cell.numericCellValue.toInt() else cell.numericCellValue
    }
}
