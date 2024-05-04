package com.example.MinecraftAPI.models

import com.example.MinecraftAPI.utils.block
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import jakarta.persistence.*
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*
import java.util.concurrent.TimeUnit

@Entity
@Table(name = "users")
data class MinecraftUser(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_ID", unique = true)
    val id: Long = -1,
    @Column(name = "username", unique = true)
    val username: String,
    @Column(name = "password")
    var password: String,
    @Column(name = "telegram_id", unique = true)
    val telegramId: String,
    @Column(name = "register_at")
    val registerAt: LocalDateTime,
    @Column(name = "subscribe_end",)
    var subscribeEnd: LocalDateTime? = null,
    @Column(name = "uuid", unique = true)
    val uuid: String,
    @Column(name = "accesstoken")
    val accessToken: String?,
    @Column(name = "serverID")
    val serverID: String?,
){
    val hasSubscription:Boolean
        get() {
            return getSubscriptionDays() != null
        }

    private fun getSubscriptionDays() : Long? {
        val daysUntil = subscribeEnd?.let {
            val days = TimeUnit.MILLISECONDS.toDays(it.toInstant(ZoneOffset.UTC).toEpochMilli() - Instant.now().toEpochMilli())
            if(days < 0){
                null
            }else{
                days
            }
        }
        return daysUntil
    }
    fun addSubscriptionDays(days:Int){
        this.subscribeEnd = block {
            val currentStamp = if (hasSubscription) {
                Timestamp.from((subscribeEnd!!).toInstant(ZoneOffset.UTC))
            } else {
                Timestamp.valueOf(LocalDateTime.now())
            }
            return@block currentStamp.toLocalDateTime().plusDays(days.toLong())
        }
    }
}