package com.example.MinecraftAPI.models
data class UploadFileResponse(
    val fileName: String,
    val fileDownloadUrl: String,
    val fileType: String,
    val fileSize: Long = -1L,
)
