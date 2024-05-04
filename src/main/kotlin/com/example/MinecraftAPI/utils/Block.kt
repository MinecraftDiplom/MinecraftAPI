package com.example.MinecraftAPI.utils

inline fun <T> block(block:() -> T) : T {
    return block()
}