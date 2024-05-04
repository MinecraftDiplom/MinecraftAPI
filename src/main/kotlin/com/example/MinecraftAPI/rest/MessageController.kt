package com.example.MinecraftAPI.rest

import com.example.MinecraftAPI.models.Message
import com.example.MinecraftAPI.repositories.MessageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/messages")
class MessageController(
    @Autowired val messages: MessageRepository
) {
    @GetMapping
    fun getAll(): List<Message> {
        return messages.findAll()
    }

    @GetMapping("/id")
    fun getById(@RequestParam("chat_id") chatId: Long, @RequestParam("message_id") messageId: Long): Message? {
        return messages.findMessageByMessageIdAndChatId(messageId, chatId)
    }
}