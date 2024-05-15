package com.example.MinecraftAPI

import com.example.MinecraftAPI.bot.TelegramBot
import com.example.MinecraftAPI.configuration.CloaksProperties
import com.example.MinecraftAPI.configuration.SkinsProperties
import com.example.MinecraftAPI.configuration.StorageProperties
import com.example.MinecraftAPI.service.CloakStorageService
import com.example.MinecraftAPI.service.FileStorageService
import com.example.MinecraftAPI.service.SkinsStorageService
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
@RestController
@EnableConfigurationProperties(
	StorageProperties::class,
	SkinsProperties::class,
	CloaksProperties::class
)
class MinecraftApiApplication{

	@Bean
	fun init(
		storageService: FileStorageService,
		skinsService: SkinsStorageService,
		cloakService: CloakStorageService,
	): CommandLineRunner {
		return CommandLineRunner { _: Array<String?>? ->
			storageService.init()
			skinsService.init()
			cloakService.init()
		}
	}

	@GetMapping("/")
	fun hello(): String {
		return "Hello World!"
	}
}

fun main(args: Array<String>) {
//	TelegramBot.initialise("7111060319:AAFxj4U0cqK3jSYItM-X_Skif2R235BAP8w")
	runApplication<MinecraftApiApplication>(*args)
}