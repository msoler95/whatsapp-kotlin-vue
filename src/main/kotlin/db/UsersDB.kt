package db

import io.javalin.websocket.WsConnectContext
import models.User
import kotlin.RuntimeException

class UsersDB (val users: MutableMap<String, User>) {
    fun getUser(senderID: String): User =
        this.users.get(senderID) ?: throw RuntimeException("User no exists")
    fun existUser(senderID: String): Boolean =
        this.users.get(senderID) != null
    fun createUserIfItDoesntExists(ctx: WsConnectContext, senderId: String) {
        if(!this.existUser(senderId)) {
            val user = User(ctx)
            users.put(senderId, user)
        }
    }
}