package com.example.MinecraftAPI.repositories

import com.example.MinecraftAPI.models.MinecraftUser
import org.springframework.data.mongodb.core.aggregation.AccumulatorOperators.Min
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface MinecraftUsersRepository : CrudRepository<MinecraftUser, Long> {
    fun findMinecraftUserByTelegramId(id: String): MinecraftUser?

    fun findMinecraftUserByUsername(username: String): MinecraftUser?

    fun findMinecraftUserById(id: Long): MinecraftUser?

}