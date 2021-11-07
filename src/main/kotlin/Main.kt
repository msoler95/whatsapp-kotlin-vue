import com.google.gson.Gson
import io.javalin.Javalin
import io.javalin.websocket.WsConnectContext
import io.javalin.websocket.WsMessageContext
import models.User
import db.UsersDB
import io.javalin.websocket.WsCloseContext
import models.Message
import java.lang.RuntimeException

val usersDB = UsersDB(mutableMapOf<String, User>())
lateinit var userCtx :WsConnectContext ;
fun main() {
    val app = Javalin.create().start(7001)
    app.ws("/message/") { ws ->
        ws.onConnect { ctx ->
            userCtx = ctx;
            val senderId = getQueryParam(ctx, "senderId")
            print("Contacto " + senderId + " conectado")
            usersDB.createUserIfItDoesntExists(ctx, senderId)
            setUserAsOnlineAndSaveContext(ctx, senderId)
            sendUserChats(userCtx, senderId)
        }
        ws.onClose { ctx ->
            setUserAsOffline(ctx)
        }
        ws.onMessage { ctx ->
            val senderId = getQueryParam(ctx, "senderId")
            val recieverId = getQueryParam(ctx, "recieverId")
            if  (!usersDB.existUser(recieverId))
                ctx.send("Contact no exist")
            else {
                createChatsIfTheyDidntExists(senderId, recieverId)
                sentMessage(senderId, recieverId, ctx.message())
            }
        }

    }
}

fun setUserAsOnlineAndSaveContext(ctx: WsConnectContext, senderId: String) {
    val sender = usersDB.getUser(senderId)
    sender.isOnline = true  //todo: check how to modify with setter
    sender.context = ctx
}

fun sendUserChats(ctx: WsConnectContext, senderId: String) {
    val sender = usersDB.getUser(senderId)
    ctx.send("""{ "type": "new-messages", "data": """ + sender.getMessages() + "}");
    sender.deleteMessages()
}

fun createChatsIfTheyDidntExists(senderId: String, recieverId: String) {
    val userSender: User = usersDB.getUser(senderId)
    val userReciever: User = usersDB.getUser(recieverId)
    userSender.createChatIfItDoesntExists(recieverId)
    userReciever.createChatIfItDoesntExists(senderId)
}

fun sentMessage(senderId: String, recieverId: String, message: String ) {
    val userReciever: User = usersDB.getUser(recieverId)
    //If user online, sent a message to it
    if(userReciever.isOnline == true)
        userReciever.sendMessage(message)
    //else save message and send it later when he is online
    else userReciever.saveMessage(senderId, message)
}

fun setUserAsOffline(ctx: WsCloseContext) {
    val senderId = getQueryParam(ctx, "senderId")
    val userSender: User = usersDB.getUser(senderId)
    userSender.isOnline = false;
}

fun getQueryParam(ctx: WsConnectContext, parameter: String): String =
    ctx.queryParam(parameter) ?: throw RuntimeException("missing parameter " + parameter)
fun getQueryParam(ctx: WsMessageContext, parameter: String): String =
    ctx.queryParam(parameter) ?: throw RuntimeException("missing parameter " + parameter)
fun getQueryParam(ctx: WsCloseContext, parameter: String): String =
    ctx.queryParam(parameter) ?: throw RuntimeException("missing parameter " + parameter)



