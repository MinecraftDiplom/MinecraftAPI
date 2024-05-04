package com.example.MinecraftAPI.rest

import com.example.MinecraftAPI.models.*
import com.example.MinecraftAPI.repositories.MessageRepository
import com.example.MinecraftAPI.repositories.MinecraftUsersRepository
import com.example.MinecraftAPI.repositories.PromocodesRepository
import com.example.MinecraftAPI.utils.MainLogger
import com.example.MinecraftAPI.utils.MainLogger.Companion.logger
import com.mongodb.client.model.Filters
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.time.Instant

@RestController
@RequestMapping("/profiles")
class MinecraftUsersController(
    @Autowired val profiles: MinecraftUsersRepository,
    @Autowired val promos: PromocodesRepository,
) {
    @GetMapping
    fun getAll(): List<MinecraftUser> {
        return profiles.findAll().toList()
    }

    @GetMapping("/{id}")
    fun getByUsername(@PathVariable id: String): MinecraftUser? {
        return profiles.findMinecraftUserByTelegramId(id)
    }

    @PostMapping("/password/{id}")
    fun uploadSkin(@PathVariable id: Long, @RequestParam("password") password: String): ResponseEntity<MinecraftUser> {
        val profile = profiles.findMinecraftUserById(id)
        if(profile==null || profile.id != id) return ResponseEntity.notFound().build()
        profile.password = password
        return try {
            ResponseEntity.ok(profiles.save(profile))
        }catch (e: Exception){
            ResponseEntity.internalServerError().build()
        }
    }

    @PostMapping("/promo/{id}")
    fun promocodeActivate(@PathVariable id: Long, @RequestParam("code") code: String): ResponseEntity<String> {
        val promo = promos.findPromocodeByCode(code) ?: return ResponseEntity.notFound().build()
        val profile = profiles.findMinecraftUserById(id) ?:  return ResponseEntity.notFound().build()

        if(promo.activated_at != null || promo.activated_to != null){
            return ResponseEntity.badRequest().build()
        }

        val split = promo.details.split(":")
        val action = split[0]
        val reward = split[1]

        when(action){
            "sub" -> {
                profile.addSubscriptionDays(reward.toInt())
                promos.save(
                    promo.apply {
                        activated_at = Instant.now().toEpochMilli()
                        activated_to = profile
                    }
                )
                profiles.save(profile)
                logger.info("${profile.username} активировал промокод - $code")
                return ResponseEntity.ok("Промокод на $reward дней успешно активирован.")
            }
            else ->{
                return ResponseEntity.badRequest().build()
            }
        }
    }
}