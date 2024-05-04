package com.example.MinecraftAPI.rest.tutorial

import com.example.MinecraftAPI.models.UploadFileResponse
import com.example.MinecraftAPI.service.FileStorageService
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.io.IOException
import java.nio.file.Files
import java.util.Arrays
import java.util.stream.Collectors
import kotlin.io.path.Path

const val FAKE_PATH_URL = "fake"

@RestController
class FileUploadRestController(
    private val fileStorageService: FileStorageService
) {
    private val logger: Logger = LoggerFactory.getLogger(FileUploadRestController::class.java)

    @PostMapping("/uploadFile")
    fun createFile(@RequestParam("file") file: MultipartFile): UploadFileResponse {
        val fileName: String = fileStorageService.saveFile(file)
        val fileDownloadUri = getFileDownloadUri(fileName)
        return UploadFileResponse(
            fileName,
            fileDownloadUri,
            file.contentType ?: "",
            file.size
        )
    }

    @GetMapping("/$FAKE_PATH_URL/{fileName:.+}")
    fun getFile(
        @PathVariable fileName: String,
        request: HttpServletRequest
    ): ResponseEntity<Resource> {
        // Load file as Resource
        val resource: Resource = fileStorageService.loadAsResources(fileName)

        val contentType = try {
            request.servletContext.getMimeType(resource.file.absolutePath)
        } catch (ex: IOException) {
            logger.info("Could not determine file type.")
            "application/octet-stream"
        }
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.filename + "\"")
            .body(resource)
    }

    @GetMapping("/getFiles")
    fun getFiles(): List<UploadFileResponse> = fileStorageService.loadAll().map { path ->
        val filePath = StringUtils.cleanPath(path.toAbsolutePath().toString())
        UploadFileResponse(
            path.fileName.toString(),
            getFileDownloadUri(path.toString()),
            Files.probeContentType(Path(filePath))
        )
    }.collect(Collectors.toList())

    @PostMapping("/uploadMultipleFiles")
    fun uploadMultipleFiles(@RequestParam("files") files: Array<MultipartFile>): MutableList<UploadFileResponse?>? {
        return Arrays.stream(files)
            .map(this::createFile)
            .collect(Collectors.toList());
    }

    private fun getFileDownloadUri(fileName: String) = ServletUriComponentsBuilder.fromCurrentContextPath()
        .path("/$FAKE_PATH_URL/")
        .path(fileName)
        .toUriString()
}