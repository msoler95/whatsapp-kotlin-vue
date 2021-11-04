package models
import io.javalin.websocket.WsConnectContext

class User (
    var context: WsConnectContext,
    var isOnline: Boolean,
    var chats: MutableMap<String, MutableList<String>>,
) {
    fun getChat(contactId: String): MutableList<String>? {
        return this.chats.get(contactId)
    }
    fun chatExists(contactId: String): Boolean? {
        return this.getChat(contactId) != null
    }
    fun createChatIfItDoesntExists(contactId: String)   {
        if(!this.chatExists(contactId)!!)
            this.chats.set(contactId, mutableListOf<String>() )
    }
    fun sendMessage(message: String) {
        this.context.send(message)
    }
}