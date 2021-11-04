import io.javalin.Javalin
import io.javalin.websocket.WsConnectContext
import io.javalin.websocket.WsMessageContext
import models.User
import db.UsersDB
import io.javalin.websocket.WsCloseContext

val usersDB = UsersDB(mutableMapOf<String, User>())
var idInit = 0;

fun main() {
    val app = Javalin.create().start(7001)
    app.ws("/message/") { ws ->
        ws.onConnect { ctx ->
            usersDB.createUserIfItDoesntExists(ctx)
            setUserAsOnlineAndSaveContext(ctx)
            sendUserChats(ctx)
        }
        ws.onMessage { ctx ->
            val senderId = ctx.queryParam("senderId")!!
            val recieverId = ctx.queryParam("recieverId")!!
            if(!usersDB.existUser(recieverId))
                ctx.send("Contact no exist")
            else {
                createChatsIfTheyDidntExists(senderId, recieverId)
                sentMessage(senderId, recieverId, ctx.message())
            }
        }
        ws.onClose { ctx ->
            setUserAsOffline(ctx)
        }
    }
}

fun setUserAsOnlineAndSaveContext(ctx: WsConnectContext) {
    val sender = usersDB.getUser(ctx.queryParam("senderId")!!)
    sender?.isOnline = true
    sender?.context = ctx
}

fun sendUserChats(ctx: WsConnectContext) {
    val sender = usersDB.getUser(ctx.queryParam("senderId")!!)
    ctx.send(sender!!.getMessages())
    sender.deleteMessages()
}

fun createChatsIfTheyDidntExists(senderId: String, recieverId: String) {
    val userSender: User? = usersDB.getUser(senderId)
    val userReciever: User? = usersDB.getUser(recieverId)
    userSender?.createChatIfItDoesntExists(recieverId)
    userReciever?.createChatIfItDoesntExists(senderId)
}

fun sentMessage(senderId: String, recieverId: String, message: String ) {
    val userReciever: User? = usersDB.getUser(recieverId)
    //If user online, sent a message to it
    if(userReciever?.isOnline == true)
        userReciever.sendMessage(message)
    //else save message and send it later when he is online
    else userReciever?.saveMessage(senderId, message)
}

fun setUserAsOffline(ctx: WsCloseContext) {
    val senderId = ctx.queryParam("senderId")!!
    val userSender: User? = usersDB.getUser(senderId)
    userSender!!.isOnline = false;
}

