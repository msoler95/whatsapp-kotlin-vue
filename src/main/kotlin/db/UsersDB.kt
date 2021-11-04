package db

import io.javalin.websocket.WsConnectContext
import models.User

class UsersDB (val users: MutableMap<String, User>) {
    fun getUser(senderID: String): User? {
        return this.users.get(senderID)
    }
    fun existUser(senderID: String): Boolean {
        return this.getUser(senderID) != null
    }
    fun createUserIfItDoesntExists(ctx: WsConnectContext) {
        val senderId = ctx.queryParam("senderId")!!
        if(!this.existUser(senderId)) {
            val user = User(ctx, true, mutableMapOf<String, MutableList<String>>() )
            users.put(senderId, user)
        }
    }
}