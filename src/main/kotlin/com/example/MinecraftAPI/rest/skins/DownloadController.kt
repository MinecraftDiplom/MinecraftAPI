package com.example.MinecraftAPI.rest.skins

import com.example.MinecraftAPI.configuration.StorageProperties
import com.example.MinecraftAPI.exception.StorageException
import com.example.MinecraftAPI.models.UploadFileResponse
import com.example.MinecraftAPI.service.FileStorageService
import com.example.MinecraftAPI.utils.MainLogger.Companion.logger
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import java.util.stream.Collectors
import kotlin.io.path.Path

const val FAKE_PATH_URL = "fake"
const val DOWNLOAD_APK_DIR = "download/apks"
@RestController
@RequestMapping("/files")
class DownloadController(
    properties: StorageProperties,
    private val storage: FileStorageService,
) {
    init {
        try {
            val path = Paths.get("${properties.location}/$DOWNLOAD_APK_DIR")
            Files.createDirectories(path)
        } catch (exception: IOException) {
            throw StorageException("Could not initialize storage")
        }
    }

    @GetMapping("/download/apk")
    fun downloadApk(): ResponseEntity<Any> {
        val resource: Resource = storage.loadAsResources("download/apks/MineProfile-arm64.apk")

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.filename + "\"")
            .header(HttpHeaders.CONTENT_TYPE, "application/vnd.android.package-archive")
            .body(resource)
    }


    @PostMapping("/uploadFile")
    fun createFile(@RequestParam("file") file: MultipartFile): UploadFileResponse {
        val fileName: String = storage.saveFile(file)
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
        val resource: Resource = storage.loadAsResources(fileName)

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
    fun getFiles(): List<UploadFileResponse> = storage.loadAll().map { path ->
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