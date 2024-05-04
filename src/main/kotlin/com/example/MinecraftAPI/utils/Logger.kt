package com.example.MinecraftAPI.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class Log {
    val logger: Logger = LoggerFactory.getLogger(this.javaClass)
}

class MainLogger {
    companion object : Log() {
        fun telegramError(message: String) {
            logger.error(message)
//            telegramBot.sendMessage(ChatId.fromId(UserIds.koliy82.id), message)
        }

    }

}