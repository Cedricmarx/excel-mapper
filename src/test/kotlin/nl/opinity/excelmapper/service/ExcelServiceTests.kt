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
    private val testFile = "SampleData"
    private val inputFile = FileInputStream("src/test/resources/$testFile.xlsx")

    @Mock
    lateinit var mongoService: MongoService

    @BeforeEach
    fun init() {
        MockitoAnnotations.openMocks(this)
        excelService = ExcelServiceImpl(mongoService)
    }

    @Test
    fun convertToObjectAndStoreShouldThrowExceptionWhenFileHasWrongExtension() {
        val file = MockMultipartFile("data", "$testFile.docx", "text/plain", inputFile)

        val exception = assertThrows<ExcelException> { excelService.convertToObjectAndStore(file) }
        assertEquals("$testFile doesn't have a valid excel extension {docx}!", exception.message)
    }

    @Test
    fun convertToObjectAndStoreShouldThrowExceptionWhenFileHasInvalidPathSequence() {
        val inputFile = FileInputStream("src/test/resources/$testFile.xlsx")
        val invalidFileName = testFile.plus("&%+?/.xlsx")
        val file = MockMultipartFile("data", invalidFileName, "text/plain", inputFile)

        val exception = assertThrows<ExcelException> { excelService.convertToObjectAndStore(file) }
        assertEquals("$invalidFileName contains invalid path sequence!", exception.message)
    }

    @Test
    fun convertToObjectAndStoreShouldPassWithValidExcelFile() {
        val file = MockMultipartFile("data", testFile, "text/plain", inputFile)

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