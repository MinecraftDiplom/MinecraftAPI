package com.example.MinecraftAPI.rest

import com.example.MinecraftAPI.models.FamilyTree
import com.example.MinecraftAPI.models.TelegramUser
import com.example.MinecraftAPI.repositories.BrakRepository
import com.example.MinecraftAPI.repositories.TelegramUsersRepository
import com.example.MinecraftAPI.utils.MainLogger.Companion.logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/tree")
class FamilyTreeController(
    @Autowired val braks: BrakRepository,
    @Autowired val users: TelegramUsersRepository,
) {
    fun createTree(user: TelegramUser): FamilyTree {
        var brak = braks.findBrakByFirstUserIDOrSecondUserID(user.id, user.id) ?: return FamilyTree(user)
        val tree = FamilyTree(user)
        createTreeRecursive(user, tree)
        return tree
    }

    private fun createTreeRecursive(user: TelegramUser, tree: FamilyTree) {
        val brak = braks.findBrakByFirstUserIDOrSecondUserID(user.id, user.id) ?: return
        val partnerID = if (brak.firstUserID == user.id) brak.secondUserID else brak.firstUserID
        val partner = users.findTelegramUserById(partnerID) ?: return
        tree.value = user
        tree.left = FamilyTree(partner)
        if (brak.baby != null) {
            val child = users.findTelegramUserById(brak.baby!!.userID) ?: return
            tree.right = FamilyTree(child)
            createTreeRecursive(child, tree.right!!)
        }
    }
//    @GetMapping
//    fun getAll(): List<FamilyTree> {
//        return braks.findAll()
//    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<FamilyTree> {
        val user = users.findTelegramUserById(id) ?: return ResponseEntity.notFound().build()
        val tree = createTree(user)
        logger.info("${user.username} просматривает своё семейное древо")
        return ResponseEntity.ok(tree)
    }
}