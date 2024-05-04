package com.example.MinecraftAPI.rest

import com.example.MinecraftAPI.bot.TelegramBot
import com.example.MinecraftAPI.models.BrakDTO
import com.example.MinecraftAPI.models.TelegramCode
import com.example.MinecraftAPI.repositories.BrakRepository
import com.example.MinecraftAPI.repositories.MinecraftUsersRepository
import com.example.MinecraftAPI.repositories.TelegramUsersRepository
import com.example.MinecraftAPI.utils.MainLogger
import com.example.MinecraftAPI.utils.MainLogger.Companion.logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.random.Random

@RestController
@RequestMapping("/auth")
class AuthCodeController(
    @Autowired val users: TelegramUsersRepository,
    @Autowired val profiles: MinecraftUsersRepository,
    @Autowired val braks: BrakRepository,
) {

    @GetMapping("/code")
    fun generate(): String {
        return (Random.nextInt(9000) + 1000).toString()
    }

    @GetMapping("/code/{nick}")
    fun generateForUser(@PathVariable nick: String): ResponseEntity<TelegramCode> {
        val user = users.findTelegramUserByUsername(nick) ?: return ResponseEntity.notFound().build()

        val profile = profiles.findMinecraftUserByTelegramId(user.id.toString()) ?: return ResponseEntity.notFound().build()

        val code = TelegramCode.generate(user, profile.apply { password = "***" })

        if(TelegramBot.sendCode(code).isError)
            return ResponseEntity.internalServerError().build()
        logger.info("Кто-то попытался войти в аккаунт $nick")
        return ResponseEntity.ok(code)
    }

    @GetMapping("/data/{nick}")
    fun userData(@PathVariable nick: String): ResponseEntity<Any> {
        val user = users.findTelegramUserByUsername(nick) ?: return ResponseEntity.notFound().build()
        val profile = profiles.findMinecraftUserByTelegramId(user.id.toString()) ?: return ResponseEntity.notFound().build()
        val brak = braks.findBrakByFirstUserIDOrSecondUserID(user.id, user.id)
            ?: return ResponseEntity.ok(TelegramCode(user, profile.apply { password = "***" }, null, null))

        val dto = BrakDTO(
            firstUser = users.findTelegramUserById(brak.firstUserID),
            secondUser = users.findTelegramUserById(brak.secondUserID),
            baby = if(brak.baby != null) users.findTelegramUserById(brak.baby!!.userID) else null,
            brak.time
        )

        logger.info("$nick просмотрел свою информацию")

        return ResponseEntity.ok(TelegramCode(user, profile.apply { password = "***" }, dto, null))
    }

}