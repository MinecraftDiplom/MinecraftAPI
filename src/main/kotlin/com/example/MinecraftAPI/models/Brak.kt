package com.example.MinecraftAPI.models

import org.springframework.data.annotation.Id
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document

@Document("braks")
data class Brak(
    var firstUserID: Long,
    var secondUserID: Long,
    var baby: Baby? = null,
    var time: Long,
    @Id
    var _id: ObjectId? = null,
)
