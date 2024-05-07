package com.example.MinecraftAPI.service

import com.example.MinecraftAPI.models.UploadFileResponse
import org.springframework.core.io.Resource
import org.springframework.http.ResponseEntity
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Path
import java.util.stream.Stream

interface SkinsStorageService {
    fun init()
    fun saveSkin(username: String, file: MultipartFile): ResponseEntity<Any>
    fun loadAll(): Stream<Path>
    fun load(fileName: String): Path
    fun loadAsResources(fileName: String): Resource?
    fun deleteSkin(resource: Resource): Boolean
    fun deleteAll()
}