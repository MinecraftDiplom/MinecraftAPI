package com.example.MinecraftAPI.service

import com.example.MinecraftAPI.configuration.StorageProperties
import com.example.MinecraftAPI.exception.StorageException
import com.example.MinecraftAPI.exception.StorageFileNotFoundException
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.util.FileSystemUtils
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.net.MalformedURLException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.stream.Stream

@Service
class FileStorageServiceImpl(
    properties: StorageProperties
) : FileStorageService {
    private val rootLocation = Paths.get(properties.location)

    override fun init() {
        try {
            Files.createDirectories(rootLocation)
        } catch (exception: IOException) {
            throw StorageException("Could not initialize storage")
        }
    }

    override fun saveFile(file: MultipartFile): String {
        return try {
            val originalFilename = file.originalFilename ?: ""
            if (file.isEmpty || originalFilename.isBlank())
                throw StorageException("Failed to store empty file.")

            val destinationFile = rootLocation.resolve(
                Paths.get(originalFilename)
            ).normalize().toAbsolutePath()

            if (destinationFile.parent != rootLocation.toAbsolutePath()) {
                throw StorageException("Cannot store file outside current directory")
            }

            file.inputStream.use { inputStream ->
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING)
            }
            StringUtils.cleanPath(originalFilename)
        } catch (exception: IOException) {
            throw StorageException("Failed to read stored files", exception)
        }
    }

    override fun loadAll(): Stream<Path> =
        try {
            Files.walk(rootLocation, 1)
                .filter { path -> path != rootLocation }
                .map(rootLocation::relativize)
        } catch (exception: IOException) {
            throw StorageException("Failed to read stored files", exception)
        }

    override fun load(fileName: String): Path = rootLocation.resolve(fileName)
    override fun loadAsResources(fileName: String): Resource = try {
        val resource: Resource = UrlResource(load(fileName).toUri())
        if (resource.exists() || resource.isReadable) {
            resource
        } else {
            throw StorageFileNotFoundException("Could not read file: $fileName")
        }
    } catch (e: MalformedURLException) {
        throw StorageFileNotFoundException("Could not read file: $fileName", e)
    }

    override fun deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }
}