package com.example.MinecraftAPI.models

import kotlin.random.Random

data class TelegramCode(
    val user: TelegramUser,
    val profile: MinecraftUser,
    val brak: BrakDTO?,
    val code: Int?
){
    companion object{
        fun generate(user: TelegramUser, profile: MinecraftUser): TelegramCode{
            return TelegramCode(user, profile, null, Random.nextInt(9000) + 1000)
        }
    }
}
