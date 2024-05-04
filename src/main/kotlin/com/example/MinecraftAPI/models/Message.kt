package com.example.MinecraftAPI.models

import com.github.kotlintelegrambot.entities.ChatLocation
import com.github.kotlintelegrambot.entities.ChatPermissions
import com.github.kotlintelegrambot.entities.files.ChatPhoto
import com.github.kotlintelegrambot.entities.files.PhotoSize
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id

@org.springframework.data.mongodb.core.mapping.Document("messages")
data class Message(
    val messageId: Long,
    val from: User? = null,
    val senderChat: Chat? = null,
    val date: Long,
    val chat: Chat,
    val forwardFrom: User? = null,
    val forwardFromChat: Chat? = null,
    val forwardFromMessageId: Int? = null,
    val forwardDate: Int? = null,
    val replyToMessage: Message? = null,
    val viaBot: User? = null,
    val editDate: Int? = null,
    val mediaGroupId: String? = null,
    val authorSignature: String? = null,
    val text: String? = null,
    val audio: Audio? = null,
    val document: Document? = null,
    val animation: Animation? = null,
    val diceEmoji: String? = null,
    val diceValue: Int? = null,
    val photo: List<PhotoSize>? = null,
    val video: Video? = null,
    val voice: Voice? = null,
    val videoNote: VideoNote? = null,
    val caption: String? = null,
    @Id
    val _id: ObjectId? = null,
)

data class User(
    val id: Long? = null,
    val isBot: Boolean? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val username: String? = null,
    val languageCode: String? = null,
    val canJoinGroups: Boolean? = null,
    val canReadAllGroupMessages: Boolean? = null,
    val supportsInlineQueries: Boolean? = null,
)

data class Chat(
    val id: Long? = null,
    val type: String? = null,
    val title: String? = null,
    val username: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val photo: ChatPhoto? = null,
    val bio: String? = null,
    val description: String? = null,
    val inviteLink: String? = null,
    val pinnedMessage: String? = null,
    val permissions: ChatPermissions? = null,
    val slowModeDelay: Int? = null,
    val stickerSetName: String? = null,
    val canSetStickerSet: Boolean? = null,
    val linkedChatId: Long? = null,
    val location: ChatLocation? = null,
)

data class Audio(
    val fileId: String? = null,
    val fileUniqueId: String? = null,
    val duration: Int? = null,
    val performer: String? = null,
    val title: String? = null,
    val mimeType: String? = null,
    val fileSize: Int? = null,
    val thumb: PhotoSize? = null,
    val fileName: String? = null,
)

data class Document(
    val fileId: String? = null,
    val fileUniqueId: String? = null,
    val thumb: PhotoSize? = null,
    val fileName: String? = null,
    val mimeType: String? = null,
    val fileSize: Int? = null,
)

data class Animation(
    val fileId: String? = null,
    val fileUniqueId: String? = null,
    val width: Int? = null,
    val height: Int? = null,
    val duration: Int? = null,
    val thumb: PhotoSize? = null,
    val fileName: String? = null,
    val mimeType: String? = null,
    val fileSize: Long? = null,
)

data class Video(
   val fileId: String,
    val fileUniqueId: String,
    val width: Int,
    val height: Int,
    val duration: Int,
    val thumb: PhotoSize? = null,
    val mimeType: String? = null,
    val fileSize: Int? = null,
    val fileName: String? = null,
)

data class Voice(
    val fileId: String,
    val fileUniqueId: String,
    val duration: Int,
    val mimeType: String? = null,
    val fileSize: Int? = null,
)

data class VideoNote(
    val fileId: String,
    val fileUniqueId: String,
    val length: Int,
    val duration: Int,
    val thumb: PhotoSize? = null,
    val fileSize: Int? = null,
)