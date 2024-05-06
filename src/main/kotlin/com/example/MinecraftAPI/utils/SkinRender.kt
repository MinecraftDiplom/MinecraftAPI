package com.example.MinecraftAPI.utils

import org.springframework.core.io.Resource
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate

enum class RenderMode(val mapping: String){
    FULLBODY("fullbody"),
    FULLBODYISO("fullbodyiso"),
    BodyBust("bodybust"),
    FrontBust("frontbust"),
    FRONTFULL("frontfull"),
    HEAD("head"),
    HEADISO("headiso"),
    FACE("face"),
    SKIN("skin"),
    CUSTOM("custom"),
}
fun Resource.skinRender(mode: RenderMode, client: RestTemplate): ResponseEntity<ByteArray> {
    val headers = HttpHeaders()
    headers.contentType = MediaType.MULTIPART_FORM_DATA

    val body = LinkedMultiValueMap<String, Any>()
    body.add("skin", this)

    val requestEntity = HttpEntity<MultiValueMap<String, Any>>(body, headers)
    return client.postForEntity("http://skin:8088/${mode.mapping}", requestEntity, ByteArray::class.java)
}