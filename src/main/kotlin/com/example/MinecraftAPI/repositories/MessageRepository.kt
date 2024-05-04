package com.example.MinecraftAPI.repositories

import com.example.MinecraftAPI.models.Message
import com.example.MinecraftAPI.models.TelegramUser
import org.springframework.data.mongodb.repository.MongoRepository

interface MessageRepository : MongoRepository<Message, String> {
    fun findMessageByMessageIdAndChatId(messageId: Long, chatId: Long) : Message?
}