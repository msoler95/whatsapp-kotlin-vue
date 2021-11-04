import io.javalin.Javalin
import io.javalin.websocket.WsConnectContext
import io.javalin.websocket.WsMessageContext
import models.User
import db.UsersDB

val usersDB = UsersDB(mutableMapOf<String, User>())
var idInit = 0;

fun main() {
    val app = Javalin.create().start(7001)
    app.ws("/message/") { ws ->
        ws.onConnect { ctx ->
            usersDB.createUserIfItDoesntExists(ctx)
            //Set user as online and notify new messages
            val sender = usersDB.getUser(ctx.queryParam("senderId")!!)
            sender?.isOnline = true
            sender?.context = ctx
            ctx.send(sender!!.getMessages())
        }
        ws.onMessage { ctx ->
            val senderId = ctx.queryParam("senderId")!!
            val recieverId = ctx.queryParam("recieverId")!!
            if(!usersDB.existUser(recieverId))
                ctx.send("Contact no exist")
            else {
                val userSender: User? = usersDB.getUser(senderId)
                val userReciever: User? = usersDB.getUser(recieverId)
                userSender?.createChatIfItDoesntExists(recieverId)
                userReciever?.createChatIfItDoesntExists(senderId)
                //If user online, sent a message to it
                if(userReciever?.isOnline == true)
                    userReciever.sendMessage(ctx.message())
                //else save message and send it later when he is online
                else userReciever?.saveMessage(senderId, ctx.message())
            }
        }
        ws.onClose { ctx ->
            val senderId = ctx.queryParam("senderId")!!
            val userSender: User? = usersDB.getUser(senderId)
            userSender!!.isOnline = false;
        }
    }
}


