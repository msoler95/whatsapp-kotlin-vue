package models
import com.github.underscore.lodash.Json
import com.github.underscore.lodash.Json.JsonArray
import io.javalin.websocket.WsConnectContext

class User (
    var context: WsConnectContext,
    var isOnline: Boolean = true,
    var chats: MutableMap<String, MutableList<String>> = mutableMapOf<String, MutableList<String>>(),
) {
    fun getChat(contactId: String): MutableList<String> =
        this.chats.get(contactId) ?: throw RuntimeException("Chat not found")

    fun chatExists(contactId: String): Boolean =
        this.chats.get(contactId) != null

    fun createChatIfItDoesntExists(contactId: String)   {
        if(!this.chatExists(contactId))
            this.chats.set(contactId, mutableListOf<String>() )
    }
    fun sendMessage(message: String) {
        this.context.send(message)
    }
    fun saveMessage(userSender: String, message: String) {
        val chat: MutableList<String> = getChat(userSender);
        chat.add(message)
        this.chats.set(userSender, chat)
    }
    fun getMessages(): String = Json.toJson(chats)

    fun deleteMessages() {
        this.chats.forEach {
            it.value.clear()
        }
    }
}