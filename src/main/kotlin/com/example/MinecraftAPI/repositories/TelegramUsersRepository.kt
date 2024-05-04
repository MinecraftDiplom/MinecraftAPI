package com.example.MinecraftAPI.repositories

import com.example.MinecraftAPI.models.TelegramUser
import org.springframework.data.mongodb.repository.MongoRepository

interface TelegramUsersRepository : MongoRepository<TelegramUser, String> {
    fun findTelegramUserById(id: Long) : TelegramUser?
    fun findTelegramUserByUsername(username: String) : TelegramUser?
}