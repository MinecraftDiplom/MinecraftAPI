package com.example.MinecraftAPI.rest.skins

import com.example.MinecraftAPI.models.UploadFileResponse
import com.example.MinecraftAPI.repositories.MinecraftUsersRepository
import com.example.MinecraftAPI.service.SkinsStorageService
import com.example.MinecraftAPI.utils.MainLogger.Companion.logger
import com.example.MinecraftAPI.utils.RenderMode
import com.example.MinecraftAPI.utils.skinRender
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/skins")
class SkinController(
    private val skins: SkinsStorageService,
    @Autowired val profiles: MinecraftUsersRepository,
    val client: RestTemplate,
) {
    @PostMapping("/{username}.png")
    fun uploadSkin(@PathVariable username: String, @RequestParam("file") file: MultipartFile): ResponseEntity<Any> {
        if(profiles.findMinecraftUserByUsername(username)==null) return ResponseEntity.notFound().build()
        logger.info("$username загрузил себе новый скин")
        return skins.saveSkin(username, file)
    }

    @GetMapping("/resource/{username}.png", produces = [MediaType.IMAGE_PNG_VALUE])
    fun getResourceFile(@PathVariable username: String): ResponseEntity<Resource> {
        val resource: Resource = skins.loadAsResources("$username.png")

        if (!resource.exists() || !resource.isReadable) {
            return ResponseEntity.notFound().build()
        }

        logger.info("$username просматривает своё изображение")

        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_PNG)
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.filename + "\"")
            .body(resource)
    }

    @GetMapping("/render/{mode}/{username}.png")
    fun getFullBody(@PathVariable username: String, @PathVariable mode: String): ResponseEntity<ByteArray> {
        val renderMode = RenderMode.entries.firstOrNull { it.mapping == mode } ?: return ResponseEntity.badRequest().build()

        val resource: Resource = skins.loadAsResources("$username.png")

        if (!resource.exists() || !resource.isReadable) {
            return ResponseEntity.notFound().build()
        }

        logger.info("$username просматривает свой скин")

        val response = resource.skinRender(renderMode, client)

        return ResponseEntity
            .status(response.statusCode)
            .contentType(MediaType.IMAGE_PNG)
            .body(response.body)
    }



}