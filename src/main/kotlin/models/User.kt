package models
import io.javalin.websocket.WsConnectContext
import models.Chat
class User (
    var context: WsConnectContext,
    var isOnline: Boolean,
    var chats: MutableMap<String, MutableList<String>>,
)