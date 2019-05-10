package ChaosTag.model

import akka.actor.{Actor, ActorRef, PoisonPill, Props}
import ChaosTag.model.physics.PhysicsVector


class actor extends Actor {

  var players: Map[String, ActorRef] = Map()

  val game: Game = new Game()
  var levelNumber = 0
  loadLevel(levelNumber)

  def loadLevel(levelNumber: Int): Unit ={
    val level = gameMap(levelNumber)
    game.loadLevel(level)
  }

  override def receive: Receive = {
    case message: PlayerJoined => game.PlayerJoined(message.username)
    case message: PlayerLeft => game.PlayerLeft(message.username)
    case message: MovePlayer => game.players(message.username).move(new PhysicsVector(message.x, message.y))
    case message: StopPlayer => game.players(message.username).stop()
    case UpdateGame => game.update()
    case SendGameState => sender() ! GameState(game.gameState())
  }
}
