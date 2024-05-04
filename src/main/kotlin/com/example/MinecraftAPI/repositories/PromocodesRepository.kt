package com.example.MinecraftAPI.repositories

import com.example.MinecraftAPI.models.Promocode
import org.springframework.data.mongodb.repository.MongoRepository

interface PromocodesRepository : MongoRepository<Promocode, String> {
    fun findPromocodeByCode(code: String): Promocode?
}