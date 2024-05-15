package com.example.MinecraftAPI.utils

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder.json
import org.springframework.web.client.RestTemplate
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

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
//fun Resource.skinRender(mode: RenderMode, client: RestTemplate, cape: Resource?, y: String): ResponseEntity<ByteArray> {
//    val headers = HttpHeaders()
//    headers.contentType = MediaType.MULTIPART_FORM_DATA
//
//    val body = LinkedMultiValueMap<String, Any>()
//
//    body.add("skin", this)
//    logger.info("y")
//    if (cape != null){
//        body.add("cape", this)
//        body.add("y", y)
//    }
//
//    val requestEntity = HttpEntity<MultiValueMap<String, Any>>(body, headers)
//    return client.postForEntity("http://kissota.ru:9088/${mode.mapping}", requestEntity, ByteArray::class.java)
//}

//fun Resource.skinRender(mode: RenderMode, cape: Resource?, y: String): ResponseEntity<ByteArray> {
//    val webClient = WebClient.builder().baseUrl("http://skin:8088").build()
//
//    val multipartBodyBuilder = org.springframework.http.client.MultipartBodyBuilder()
//    multipartBodyBuilder.part("skin", this.contentAsByteArray).filename("skin.png")
//
//    if (cape != null) {
//        multipartBodyBuilder.part("cape", cape.contentAsByteArray).filename("cape.png")
//    }
//
//    multipartBodyBuilder.part("y", y)
//
//    val responseMono = webClient.post()
//        .uri("/${mode.mapping}")
//        .contentType(MediaType.MULTIPART_FORM_DATA)
//        .body(BodyInserters.fromMultipartData(multipartBodyBuilder.build()))
//        .retrieve()
//        .bodyToMono<ByteArray>()
//
//    return ResponseEntity.ok(responseMono.block())
//}

suspend fun Resource.skinRenderKtor(mode: RenderMode, cape: Resource?, y: String): HttpResponse {
    val client = HttpClient(CIO) {
//        install(ContentNegotiation) {
//            json()
//        }
    }

    val response: HttpResponse =
        client.submitFormWithBinaryData(
            url = "http://skin:8088/${mode.mapping}",
            formData = formData {
                append("skin", this@skinRenderKtor.contentAsByteArray, Headers.build {
                    append(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
                    append(HttpHeaders.CONTENT_DISPOSITION, "filename=\"skin.png\"")
                })
                if (cape != null) {
                    append("cape", cape.contentAsByteArray, Headers.build {
                        append(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
                        append(HttpHeaders.CONTENT_DISPOSITION, "filename=\"cape.png\"")
                    })
                }
                append("y", y)
            }
        )

    return response
}