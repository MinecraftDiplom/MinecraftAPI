package com.example.MinecraftAPI.models

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("users")
data class TelegramUser(
    var id: Long,
    var isBot: Boolean,
    var firstName: String,
    var lastName: String? = null,
    var username: String? = null,
    var languageCode: String? = null,
    var braksCount: Int = 0,
    @Id
    var _id: ObjectId? = null,
)