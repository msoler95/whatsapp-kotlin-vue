import io.javalin.Javalin
import io.javalin.websocket.WsConnectContext

class Chats(var idUser1: Number, var idUser2: Number, var ctx: WsConnectContext) {}
val sessionsConected = mutableListOf<Chats>();
var idInit = 0;
fun main() {
    val app = Javalin.create().start(7001)
    //TODO: Poner el userId como un parametro donde se pone el id del usuario
    app.ws("/message/userId") { ws ->
        ws.onConnect { ctx ->
            val con = Chats(idInit, 1, ctx) //TODO: El 1 ha de ser el userId
            sessionsConected.add(con) //TODO: Pensar-ho be
            ctx.send("Your id is: " + idInit)
            idInit++
        }
        //Guardar
        ws.onMessage { ctx ->
            ctx.send(ctx.message());
        }
    }
}