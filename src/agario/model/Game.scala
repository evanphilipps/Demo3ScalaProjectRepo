package agario.model

import play.api.libs.json.{JsValue, Json}
import agario.model.game_objects._
import agario.model.physics.{Physics, PhysicsVector, World}
import scala.util.Random._


class Game {

  val world: World = new World(10)


  var level: Level = new Level()

  var players: Map[String, Player] = Map()
  val playerSize: Double = 0.3

  var lastUpdateTime: Long = System.nanoTime()


  def loadLevel(newLevel: Level): Unit = {
    world.boundaries = List()
    level = newLevel
    players.values.foreach(player => player.location = new PhysicsVector(nextInt(level.gridWidth-1), nextInt(level.gridHeight-1)))

  }


  def addPlayer(id: String): Unit = {
    val player = new Player(startingVector(), new PhysicsVector(0, 0))
    players += (id -> player)
    world.objects = player :: world.objects
  }


  def removePlayer(id: String): Unit = {
    players(id).destroy()
    players -= id
  }

  def blockTile(x: Int, y: Int, width: Int = 1, height: Int = 1): Unit = {
    val ul = new PhysicsVector(x, y)
    val ur = new PhysicsVector(x + width, y)
    val lr = new PhysicsVector(x + width, y + height)
    val ll = new PhysicsVector(x, y + height)

    world.boundaries ::= new Boundary(ul, ur)
    world.boundaries ::= new Boundary(ur, lr)
    world.boundaries ::= new Boundary(lr, ll)
    world.boundaries ::= new Boundary(ll, ul)
  }


  def startingVector(): PhysicsVector = {
    new PhysicsVector(nextInt(level.gridWidth-1), nextInt(level.gridHeight-1))
  }



  def update(): Unit = {
    val time: Long = System.nanoTime()
    val dt = (time - this.lastUpdateTime) / 1000000000.0
    Physics.updateWorld(this.world, dt)
    checkForPlayerHits()
    this.lastUpdateTime = time
  }

  def gameState(): String = {
    val gameState: Map[String, JsValue] = Map(
      "gridSize" -> Json.toJson(Map("x" -> level.gridWidth, "y" -> level.gridHeight)),
      "start" -> Json.toJson(Map("x" -> level.startingLocation.x, "y" -> level.startingLocation.y)),
      "players" -> Json.toJson(this.players.map({ case (k, v) => Json.toJson(Map(
        "x" -> Json.toJson(v.location.x),
        "y" -> Json.toJson(v.location.y),
        "v_x" -> Json.toJson(v.velocity.x),
        "v_y" -> Json.toJson(v.velocity.y),
        "id" -> Json.toJson(k))) })),
      )

    Json.stringify(Json.toJson(gameState))
  }




  def checkForBaseDamage(): Unit = {
    // TODO: Objective 1
  }


  def checkForPlayerHits(): Unit = {
    // TODO: Objective 3
  }


}
