package nl.opinity.excelmapper.controller

import com.google.gson.Gson
import io.swagger.annotations.*
import nl.opinity.excelmapper.service.ExcelService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/excel")
@Api(tags = ["Excel"], description = "Operations pertaining Excel documents")
class ExcelController(private val excelService: ExcelService) {

    @ApiOperation("Converts an Excel document to JSON", produces = "application/json")
    @ApiResponses(
            ApiResponse(code = 200, message = "Successfully converted Excel document to JSON",
                    examples = Example(value = [ExampleProperty(value = "{'property': 'test'}", mediaType = "application/json")])),
            ApiResponse(
                    code = 400, message = "Something went wrong while processing the Excel document",
                    examples = Example(value = [ExampleProperty(value = "Sample.docx doesn't have a valid excel extension!")])))
    @PostMapping("/convertToObject", produces = ["application/json"])
    fun uploadFile(@ApiParam("Excel document to convert to JSON")
                   @RequestParam("excel") excel: MultipartFile): ResponseEntity<Any> {
        return try {
            val jsonElementList = excelService.convertXlsxToObject(excel)
            ResponseEntity.ok().body(Gson().toJson(jsonElementList))
        } catch (e: Throwable) {
            e.printStackTrace()
            ResponseEntity.badRequest().body(e.message)
        }
    }
}