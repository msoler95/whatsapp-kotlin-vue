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
            //todo: check for new messages
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
                if(userReciever?.isOnline == true)
                    userReciever.sendMessage(ctx.message())
                //todo: else save in db
            }
        }
        ws.onClose { ctx ->
            //todo: set as user active = false
        }
    }
}


