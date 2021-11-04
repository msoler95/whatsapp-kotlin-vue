import io.javalin.Javalin
import io.javalin.websocket.WsConnectContext
import io.javalin.websocket.WsMessageContext
import models.MessageInfo
import models.Chat
import models.User


val users = mutableMapOf<String, User>();
var idInit = 0;

fun main() {
    val app = Javalin.create().start(7001)
    app.ws("/message/") { ws ->
        ws.onConnect { ctx ->
            val senderId = ctx.queryParam("senderId")!!
            if(!existUser(senderId)) {
                createUser(ctx)
                print("User created")
            } else print("User already exist")

        }
        ws.onMessage { ctx ->
            val senderId = ctx.queryParam("senderId")!!
            val recieverId = ctx.queryParam("recieverId")!!
            if(!existUser(recieverId)) {
                print("User no exists")
                ctx.send("Contact no exist")
            }

            else {
                print("User  exists")
                //W1
                if(!chatExists(senderId, recieverId)) {
                    print("New chat created  exists for " + senderId)
                    createChat(senderId, recieverId)
                }
                //W2
                if(!chatExists(recieverId, senderId)) {
                    print("New chat created  exists for " + recieverId)
                    createChat(senderId, recieverId)
                }
                if(isUserOnline(recieverId) == true) {
                    print("Sending message to " + recieverId)
                    getUserContext(recieverId)?.send(ctx.message())
                }
                //else save in db
            }
        }
        ws.onClose { ctx ->
            //set as active = false
        }
    }
}
fun getUserContext(senderId: String): WsConnectContext? {
    return getUser(senderId)?.context
}
fun createChat(senderId: String, recieverId: String) {
    val user = getUser((senderId))!!
    user.chats.set(recieverId,  mutableListOf<String>() )
}
fun createUser(ctx: WsConnectContext) {
    val senderId = ctx.queryParam("senderId")!!
    val user = User(ctx, true, mutableMapOf<String, MutableList<String>>() )
    users.put(senderId, user)
}
fun isUserOnline(userId: String?): Boolean? {
    return users.get(userId)?.isOnline
}
fun chatExists(senderId: String, recieverId: String): Boolean {
    val user = getUser((senderId))!!
    return (getChat(senderId, recieverId) != null)
}
fun getChat(senderId: String, recieverId: String): MutableList<String>? {
    val user = getUser((senderId))!!
    val chat = user.chats.get((recieverId))
    return chat
}
fun existUser(senderId: String): Boolean {
    return (users.get(senderId) != null)
}
fun getUser(senderId: String): User? {
    return users.get(senderId)
}
fun getMessageInfo(ctx: WsMessageContext): MessageInfo {
    var message: MessageInfo = MessageInfo(ctx.queryParam("sender")!!, ctx.queryParam("reciever")!!, ctx.message()!!)
    return message
}