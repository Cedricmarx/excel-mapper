package nl.opinity.excelmapper.service

import nl.opinity.excelmapper.exception.ExcelException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.mock.web.MockMultipartFile
import java.io.FileInputStream
import java.time.LocalDateTime

class ExcelServiceTests {

    private lateinit var excelService: ExcelService

    @Mock
    lateinit var mongoService: MongoService

    @BeforeEach
    fun init() {
        MockitoAnnotations.openMocks(this)
        excelService = ExcelServiceImpl(mongoService)
    }

    @Test
    fun convertToObjectAndStoreShouldThrowExceptionWhenFileHasWrongExtension() {
        val fileName = "SampleData"
        val fileExtension = "docx"
        val inputFile = FileInputStream("src/test/resources/$fileName.$fileExtension")
        val file = MockMultipartFile("data", "$fileName.$fileExtension", "text/plain", inputFile)

        val exception = assertThrows<ExcelException> { excelService.convertToObjectAndStore(file) }
        assertEquals("$fileName doesn't have a valid excel extension {$fileExtension}!", exception.message)
    }

    @Test
    fun convertToObjectAndStoreShouldThrowExceptionWhenFileHasInvalidPathSequence() {
        val fileName = "SampleData\$%!=+:?.xlsx"
        val inputFile = FileInputStream("src/test/resources/$fileName")
        val file = MockMultipartFile("data", fileName, "text/plain", inputFile)

        val exception = assertThrows<ExcelException> { excelService.convertToObjectAndStore(file) }
        assertEquals("$fileName contains invalid path sequence!", exception.message)
    }

    @Test
    fun convertToObjectAndStoreShouldPassWithValidExcelFile() {
        val fileName = "SampleData.xlsx"
        val inputFile = FileInputStream("src/test/resources/$fileName")
        val file = MockMultipartFile("data", fileName, "text/plain", inputFile)

        val response = excelService.convertToObjectAndStore(file)

        assertEquals(189.05, response[0].getValue("Total"))
        assertEquals(1.99, response[0].getValue("Unit Cost"))
        assertEquals(95.0, response[0].getValue("Units"))
        assertEquals("Pencil", response[0].getValue("Item"))
        assertEquals("Jones", response[0].getValue("Rep"))
        assertEquals("East", response[0].getValue("Region"))
        assertEquals(LocalDateTime.of(2019, 1, 6, 0, 0), response[0].getValue("OrderDate"))
    }
}