package nl.opinity.excelmapper.controller

import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import nl.opinity.excelmapper.exception.ExcelException
import nl.opinity.excelmapper.service.ExcelService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockMultipartFile
import java.io.FileInputStream

class ExcelControllerTests {

    private lateinit var excelController: ExcelController

    @Mock
    lateinit var excelService: ExcelService

    @BeforeEach
    fun init() {
        MockitoAnnotations.openMocks(this)
        excelController = ExcelController(excelService)
    }

    @Test
    fun uploadFileShouldReturn200WithValidExcelFile() {
        val inputFile = FileInputStream("src/test/resources/SampleData.xlsx")
        val file = MockMultipartFile("data", "SampleData.xlsx", "text/plain", inputFile)
        val response = excelController.uploadFile(file)
        assertEquals(HttpStatus.OK, response.statusCode)
    }

    @Test
    fun uploadFileShouldReturn400WhenServiceThrowsExcelException() {
        val inputFile = FileInputStream("src/test/resources/SampleData.xlsx")
        val file = MockMultipartFile("data", "SampleData.docx", "text/plain", inputFile)
        whenever(excelService.convertToObjectAndStore(file)).thenThrow(ExcelException("SampleData doesn't have a valid excel extension {docx}"))

        val response = excelController.uploadFile(file)
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        verify(excelService, times(1)).convertToObjectAndStore(file)
    }
}