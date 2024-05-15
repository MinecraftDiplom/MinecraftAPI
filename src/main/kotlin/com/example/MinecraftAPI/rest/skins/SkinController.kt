package com.example.MinecraftAPI.rest.skins

import com.example.MinecraftAPI.repositories.MinecraftUsersRepository
import com.example.MinecraftAPI.service.CloakStorageService
import com.example.MinecraftAPI.service.SkinsStorageService
import com.example.MinecraftAPI.utils.MainLogger.Companion.logger
import com.example.MinecraftAPI.utils.RenderMode
import com.example.MinecraftAPI.utils.skinRenderKtor
import io.ktor.client.call.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/skins")
class SkinController(
    private val skins: SkinsStorageService,
    @Autowired val profiles: MinecraftUsersRepository,
    private val capes: CloakStorageService,
    val client: RestTemplate,
) {
    private final var defaultSkin: Resource? = null;
    init {
       defaultSkin = skins.loadAsResources("default_steve_skin.png")
    }
    @PostMapping("/{username}")
    fun uploadSkin(@PathVariable username: String, @RequestParam("file") file: MultipartFile): ResponseEntity<Any> {
        logger.info("$username загружает себе новый скин")
        if(profiles.findMinecraftUserByUsername(username)==null) return ResponseEntity.notFound().build()
        return skins.saveSkin(username, file)
    }

    @GetMapping("/resource/{username}", produces = [MediaType.IMAGE_PNG_VALUE])
    fun getResourceFile(@PathVariable username: String): ResponseEntity<Resource> {
        logger.info("$username просматривает своё изображение")
        val resource: Resource = skins.loadAsResources("$username.png")?:
            return ResponseEntity.status(404).contentType(MediaType.IMAGE_PNG)
                .body(defaultSkin ?: return ResponseEntity.notFound().build())

        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_PNG)
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.filename + "\"")
            .body(resource)
    }

    @PostMapping("/remove/{username}")
    fun removeResourceFile(@PathVariable username: String): ResponseEntity<Any> {
        logger.info("$username удаляет свой скин")

        val resource: Resource = skins.loadAsResources("$username.png")?:
            return ResponseEntity.notFound().build()

        val isDeleted = skins.deleteSkin(resource)

        return if(isDeleted){
            ResponseEntity.ok().build()
        }else{
            ResponseEntity.internalServerError().build()
        }
    }

    @GetMapping("/render/{mode}/{username}")
    suspend fun getRenderSkin(
        @PathVariable username: String,
        @PathVariable mode: String,
        @RequestParam(defaultValue = "0") y: String,
        ): ResponseEntity<ByteArray> {
        logger.info("$username просматривает свой скин")
        val renderMode = RenderMode.entries.firstOrNull { it.mapping == mode } ?: return ResponseEntity.badRequest().build()
        var resource = skins.loadAsResources("$username.png")

        var isFound = true
        if(resource == null){
            resource = defaultSkin ?: return ResponseEntity.notFound().build()
            isFound = false
        }

        val cape = capes.loadAsResources("$username.png")
        val response = resource.skinRenderKtor(renderMode, cape, y)

        return ResponseEntity
            .status(response.status.value)
            .contentType(MediaType.IMAGE_PNG)
            .header("isFound", "$isFound")
            .body(response.body<ByteArray>())
    }

}