package com.example.MinecraftAPI.service

import org.springframework.core.io.Resource
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Path
import java.util.stream.Stream

interface FileStorageService {
    fun init()
    fun saveFile(file: MultipartFile): String
    fun loadAll(): Stream<Path>
    fun load(fileName: String): Path
    fun loadAsResources(fileName: String): Resource
    fun delete(resource: Resource): Boolean

    fun deleteAll()
}