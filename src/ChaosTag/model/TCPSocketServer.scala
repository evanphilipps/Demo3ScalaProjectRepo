package ChaosTag.model

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.io.{IO, Tcp}
import akka.util.ByteString
import play.api.libs.json.{JsValue, Json}



class TCPSocketServer(gameActor: ActorRef) extends Actor {

  import Tcp._
  import context.system

  IO(Tcp) ! Bind(self, new InetSocketAddress("localhost", 8000))

  var servers: Set[ActorRef] = Set()
  var buffer: String = ""
  val delimiter: String = "~"

  override def receive: Receive = {
    case b: Bound => println("Listening on port: " + b.localAddress.getPort)

    case c: Connected =>
      println("Client Connected: " + c.remoteAddress)
      this.servers = this.servers + sender()
      sender() ! Register(self)

    case PeerClosed =>
      println("Client Disconnected: " + sender())
      this.servers = this.servers - sender()

    case r: Received =>
      buffer += r.data.utf8String
      while (buffer.contains(delimiter)) {
        val curr = buffer.substring(0, buffer.indexOf(delimiter))
        buffer = buffer.substring(buffer.indexOf(delimiter) + 1)
        handleWebMsg(curr)
      }

    case SendGameState =>
      gameActor ! SendGameState

    case gs: GameState =>
      this.servers.foreach((client: ActorRef) => client ! Write(ByteString(gs.gameState + delimiter)))
  }


  def handleWebMsg(messageString:String):Unit = {
    val message: JsValue = Json.parse(messageString)
    val username = (message \ "username").as[String]
    val caseMessage = (message \ "action").as[String]

    caseMessage match {
      case "connected" => gameActor ! PlayerJoined(username)
      case "disconnected" => gameActor ! PlayerLeft(username)
      case "move" =>
        val x = (message \ "x").as[Double]
        val y = (message \ "y").as[Double]
        gameActor ! MovePlayer(username, x, y)
      case "stop" => gameActor ! StopPlayer(username)
    }
  }

}


object TCPSocketServer {

  def main(args: Array[String]): Unit = {
    val actorSystem = ActorSystem()

    import actorSystem.dispatcher

    import scala.concurrent.duration._

    val actor = actorSystem.actorOf(Props(classOf[actor]))
    val server = actorSystem.actorOf(Props(classOf[TCPSocketServer], actor))

    actorSystem.scheduler.schedule(16.milliseconds, 32.milliseconds, actor, UpdateGame)
    actorSystem.scheduler.schedule(32.milliseconds, 32.milliseconds, server, SendGameState)

  }

}
