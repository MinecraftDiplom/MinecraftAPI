package com.example.MinecraftAPI.models

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id

data class BrakDTO(
    val firstUser: TelegramUser?,
    val secondUser: TelegramUser?,
    var baby: TelegramUser? = null,
    var time: Long,
    @Id
    var _id: ObjectId? = null,
)
