package com.example.MinecraftAPI.bot

import com.example.MinecraftAPI.models.TelegramCode
import com.example.MinecraftAPI.utils.MainLogger.Companion.logger
import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.dice
import com.github.kotlintelegrambot.dispatcher.message
import com.github.kotlintelegrambot.dispatcher.telegramError
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.Message
import com.github.kotlintelegrambot.entities.ParseMode
import com.github.kotlintelegrambot.logging.LogLevel
import com.github.kotlintelegrambot.types.TelegramBotResult
import com.mongodb.client.model.Filters

object TelegramBot {

    private lateinit var bot: Bot
    fun initialise(token: String){
        this.bot = bot {
            logLevel = LogLevel.Error
            this.token = token
            timeout = 30
            dispatch {

                telegramError {
                    logger.error("[${error.getType()}] ${error.getErrorMessage()}")
                }
            }
        }

        bot.startPolling().also {
            logger.info("Telegram Bot started.")
        }
    }

    fun sendCode(code: TelegramCode): TelegramBotResult<Message> {
        return bot.sendMessage(ChatId.fromId(code.user.id), "${code.code}")
    }

}