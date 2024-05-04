package com.example.MinecraftAPI.rest

import com.example.MinecraftAPI.models.TelegramUser
import com.example.MinecraftAPI.repositories.TelegramUsersRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UsersController(
    @Autowired val users: TelegramUsersRepository
) {
    @GetMapping
    fun getAll(): List<TelegramUser> {
        return users.findAll()
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): TelegramUser? {
        return users.findTelegramUserById(id)
    }
}