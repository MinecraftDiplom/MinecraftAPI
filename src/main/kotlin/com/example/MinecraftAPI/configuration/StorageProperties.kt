package com.example.MinecraftAPI.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

const val UploadDirectory = "files"

@ConfigurationProperties("com.files.storage")
data class StorageProperties(
    val location: String
)
const val SkinsDirectory = "skins"
@ConfigurationProperties("com.skins.storage")
data class SkinsProperties(
    val location: String,
    val maxSizeMB: Int,
)

const val CloakDirectory = "cloaks"
@ConfigurationProperties("com.cloaks.storage")
data class CloaksProperties(
    val location: String,
    val maxSizeMB: Int,
)
