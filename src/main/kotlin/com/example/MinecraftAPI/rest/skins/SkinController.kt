package com.example.MinecraftAPI.rest.skins

import com.example.MinecraftAPI.models.UploadFileResponse
import com.example.MinecraftAPI.repositories.MinecraftUsersRepository
import com.example.MinecraftAPI.service.SkinsStorageService
import com.example.MinecraftAPI.utils.MainLogger
import com.example.MinecraftAPI.utils.MainLogger.Companion.logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/skins")
class SkinController(
    private val skins: SkinsStorageService,
    @Autowired val profiles: MinecraftUsersRepository
) {
    @PostMapping("/{username}.png")
    fun uploadSkin(@PathVariable username: String, @RequestParam("file") file: MultipartFile): ResponseEntity<UploadFileResponse> {
        if(profiles.findMinecraftUserByUsername(username)==null) return ResponseEntity.notFound().build()
        logger.info("$username загрузил себе новый скин")
        return skins.saveSkin(username, file)
    }

    @GetMapping("/{username}.png")
    fun getSkin(@PathVariable username: String
    ): ResponseEntity<Resource> {
        val resource: Resource = skins.loadAsResources("$username.png")
        logger.info("$username просматривает свой скин")
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.filename + "\"")
            .body(resource)
    }

}