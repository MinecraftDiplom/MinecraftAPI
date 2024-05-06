package com.example.MinecraftAPI.service

import com.example.MinecraftAPI.configuration.SkinsProperties
import com.example.MinecraftAPI.exception.StorageException
import com.example.MinecraftAPI.exception.StorageFileNotFoundException
import com.example.MinecraftAPI.models.UploadFileResponse
import com.example.MinecraftAPI.utils.RenderMode
import com.example.MinecraftAPI.utils.skinRender
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.util.FileSystemUtils
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.util.StringUtils
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForEntity
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.io.IOException
import java.io.InputStream
import java.net.MalformedURLException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.stream.Stream

@Service
class SkinsStorageServiceImpl(
    private val properties: SkinsProperties
) : SkinsStorageService {
    private val rootLocation = Paths.get(properties.location)

    override fun init() {
        try {
            Files.createDirectories(rootLocation)
        } catch (exception: IOException) {
            throw StorageException("Could not initialize storage")
        }
    }

    override fun saveSkin(username: String, file: MultipartFile): ResponseEntity<Any> {
        return try {
            if (file.isEmpty || (file.originalFilename?:"").isBlank())
                throw StorageException("Failed to store empty file.")

            if(file.contentType != "image/png"){
                return ResponseEntity.badRequest().build()
            }

            val filename = "$username.png"

            val destinationFile = rootLocation.resolve(
                Paths.get(filename)
            ).normalize().toAbsolutePath()

            if (destinationFile.parent != rootLocation.toAbsolutePath()) {
                throw StorageException("Cannot store file outside current directory")
            }

            file.inputStream.use { inputStream ->
//                val response = inputStream.renderingTest()
//                if(response.statusCode.isError){
//                    return response
//                }
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING)
            }

            StringUtils.cleanPath(filename)
            ResponseEntity.ok(UploadFileResponse(
                username,
                getFileDownloadUri(filename),
                file.contentType ?: "",
                file.size
            ))
        } catch (exception: IOException) {
            ResponseEntity.internalServerError().build()
        }
    }

//    fun InputStream.renderingTest(): ResponseEntity<Any> {
//        val headers = HttpHeaders()
//
//        headers.contentType = MediaType.MULTIPART_FORM_DATA
//        val client = RestTemplate()
//
//        val body = LinkedMultiValueMap<String, Any>()
//        val bytes = this.readBytes()
//        body.add("skin", bytes)
//
//        val requestEntity = HttpEntity<MultiValueMap<String, Any>>(body, headers)
//        return client.postForEntity("http://skin:8088/face", requestEntity)
//    }

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

    override fun deleteSkin(username: String) {
        TODO("Not yet implemented")
    }

    override fun deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    private fun getFileDownloadUri(fileName: String) = ServletUriComponentsBuilder.fromCurrentContextPath()
        .path("/${properties.location}/")
        .path(fileName)
        .toUriString()
}