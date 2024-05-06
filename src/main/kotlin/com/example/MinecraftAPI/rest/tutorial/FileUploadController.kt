package com.example.MinecraftAPI.rest.tutorial

import com.example.MinecraftAPI.configuration.UploadDirectory
import com.example.MinecraftAPI.exception.StorageFileNotFoundException
import com.example.MinecraftAPI.service.FileStorageService
import com.example.MinecraftAPI.service.SkinsStorageService
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.io.IOException
import java.util.stream.Collectors

@Controller
class FileUploadController(
    private val fileStorageService: FileStorageService,
    private val skins: SkinsStorageService,
) {

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(e: NoSuchElementException): ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.NOT_FOUND)

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleNotFound(e: IllegalArgumentException): ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.BAD_REQUEST)

    @ExceptionHandler(StorageFileNotFoundException::class)
    fun handleNotFound(e: StorageFileNotFoundException): ResponseEntity<String> =
        ResponseEntity.notFound().build()

    @PostMapping("/files")
    fun createFile(
        @RequestParam("file") file: MultipartFile,
        redirectAttributes: RedirectAttributes
    ): String {
        fileStorageService.saveFile(file)
        redirectAttributes
            .addFlashAttribute("message", "You successfully uploaded ${file.originalFilename}!")
        return "redirect:/"
    }

    @GetMapping("/$UploadDirectory/{fileName:.+}")
    fun getFile(@PathVariable fileName: String): ResponseEntity<Resource> {
        val file = fileStorageService.loadAsResources(fileName)
        return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"${file.filename}\"")
            .body(file)
    }

    @GetMapping("/files")
    @Throws(IOException::class)
    fun getFiles(model: Model): String {
        model.addAttribute(
            "files",
            fileStorageService.loadAll().map { path ->
                MvcUriComponentsBuilder.fromMethodName(
                    FileUploadController::class.java,
                    "getFile",
                    path.fileName.toString()
                ).build().toUriString()
            }.collect(Collectors.toList())
        )
        return "uploadForm"
    }
}