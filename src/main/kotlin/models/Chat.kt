package models

import io.javalin.websocket.WsConnectContext

class Chat(
    var user: String,
    var newMessages: MutableList<String>
) {}