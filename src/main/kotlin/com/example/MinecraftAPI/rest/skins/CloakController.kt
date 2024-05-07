package com.example.MinecraftAPI.rest.skins

import com.example.MinecraftAPI.models.UploadFileResponse
import com.example.MinecraftAPI.repositories.MinecraftUsersRepository
import com.example.MinecraftAPI.service.CloakStorageService
import com.example.MinecraftAPI.service.SkinsStorageService
import com.example.MinecraftAPI.utils.MainLogger
import com.example.MinecraftAPI.utils.MainLogger.Companion.logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/cloaks")
class CloakController(
    private val cloaks: CloakStorageService,
    @Autowired val profiles: MinecraftUsersRepository
) {
    private final var defaultCloak: Resource? = null;
    init {
        defaultCloak = cloaks.loadAsResources("default_not_found_cloak.png")
    }
    @PostMapping("/{username}")
    fun uploadCloak(@PathVariable username: String, @RequestParam("file") file: MultipartFile): ResponseEntity<UploadFileResponse> {
        if(profiles.findMinecraftUserByUsername(username)==null) return ResponseEntity.notFound().build()
        logger.info("$username загрузил себе новый плащ")
        return cloaks.saveCloak(username, file)
    }

    @GetMapping("/{username}")
    fun getCloak(@PathVariable username: String
    ): ResponseEntity<Resource> {
        var resource = cloaks.loadAsResources("$username.png")

        var isFound = true
        if(resource == null){
            resource = defaultCloak ?: return ResponseEntity.notFound().build()
            isFound = false
        }

        logger.info("$username просматривает свой плащ")
        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_PNG)
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.filename + "\"")
            .header("isFound", "$isFound")
            .body(resource)
    }

    @PostMapping("/remove/{username}")
    fun removeResourceFile(@PathVariable username: String): ResponseEntity<Any> {
        logger.info("$username удаляет свой скин")
        val resource: Resource = cloaks.loadAsResources("$username.png")?:
            return ResponseEntity.notFound().build()

        val isDeleted = cloaks.deleteCloak(resource)

        return if(isDeleted){
            ResponseEntity.ok().build()
        }else{
            ResponseEntity.internalServerError().build()
        }
    }
}