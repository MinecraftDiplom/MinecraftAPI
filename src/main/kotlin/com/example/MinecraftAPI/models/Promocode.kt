package com.example.MinecraftAPI.models

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("promo")
data class Promocode (
    var code: String,
    var details: String = "sub:30",
    var activated_to: MinecraftUser? = null,
    var created_at: Long? = Instant.now().toEpochMilli(),
    var activated_at: Long? = null,
    var _id: ObjectId? = null,
)