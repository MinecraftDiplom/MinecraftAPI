package com.example.MinecraftAPI.repositories

import com.example.MinecraftAPI.models.Brak
import org.springframework.data.mongodb.repository.MongoRepository

interface BrakRepository : MongoRepository<Brak, String> {
    fun findBrakByFirstUserIDOrSecondUserID(firstUserID: Long, secondUserID: Long): Brak?
}