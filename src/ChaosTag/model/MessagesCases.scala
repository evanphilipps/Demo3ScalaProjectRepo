package ChaosTag.model


// Received by Multiple Types
case object SendGameState
case class GameState(gameState: String)


// Received by GameActor
case object UpdateGame
case class PlayerJoined(username: String)
case class PlayerLeft(username: String)
case class MovePlayer(username: String, x: Double, y:Double)
case class StopPlayer(username: String)
case class LoadLevel(levelNumber: Int)
