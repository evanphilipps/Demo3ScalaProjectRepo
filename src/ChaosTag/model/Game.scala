package ChaosTag.model

import play.api.libs.json.{JsValue, Json, Writes}
import ChaosTag.model.game_objects._
import ChaosTag.model.physics.{Physics, PhysicsVector, World}
import util.control.Breaks._
import scala.collection.mutable.ListBuffer
import scala.util.Random._

class Game {

  val world: World = new World(10)


  var level: gameMap = new gameMap()
  val startSize: Double = .5
  var players: Map[String, Player] = Map()
  var food: Map[Int, Food] = Map(nextInt -> new Food(new PhysicsVector(level.gridWidth/2, level.gridHeight/2), new PhysicsVector(0, 0)))

  var lastUpdateTime: Long = System.nanoTime()


  def loadLevel(newLevel: gameMap): Unit = {
    world.boundaries = List()
    level = newLevel
    blockTile(0, 0, level.gridWidth, level.gridHeight)
    players.values.foreach(player => player.location = new PhysicsVector(nextInt(level.gridWidth-1), nextInt(level.gridHeight-1)))
  }


  def PlayerJoined(id: String): Unit = {
    val player = new Player(startingVector(), new PhysicsVector(0, 0))
    player.size = startSize
    players += (id -> player)
    world.objects = player :: world.objects
  }


  def PlayerLeft(id: String): Unit = {
    players(id).destroy()
    players -= id
  }
  def endGame(): Unit = {
    val boardArea = level.gridWidth*level.gridHeight
    val criticalMass = boardArea/3
    for(player <- players){
      if(Math.PI*Math.pow(player._2.size, 2) >= criticalMass){
        for(player <- players){
          player._2.size = startSize
        }
      }
    }
  }

  def makeFood(): Unit = {
    while(food.size < 20){
      val newFood = new Food(new PhysicsVector(nextDouble*level.gridWidth, nextDouble*level.gridHeight), new PhysicsVector(0, 0))
      food += (nextInt -> newFood)
    }
  }

  def eatFood(): Unit = {
    for(player <- players){
      val px = player._2.location.x
      val py = player._2.location.y
      for(nom <- food){
        if(calcDist(player._2.location, nom._2.location) < player._2.size) {
          food -= nom._1
          nom._2.destroy()
          player._2.size += .1
        }
      }
    }
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
    new PhysicsVector(nextInt(level.gridWidth-2)+1, nextInt(level.gridHeight-2)+1)
  }

  def eatPlayer(): Unit = {
    for(player1 <- players){
      val p1id = player1._1
      val p1size = player1._2.size
      val p1x = player1._2.location.x
      val p1y = player1._2.location.y
      for(player2 <- players){
        val p2id = player2._1
        val p2size = player2._2.size
        val p2x = player2._2.location.x
        val p2y = player2._2.location.y
        if(p1id != p2id){
          if(calcDist(player1._2.location, player2._2.location) < player1._2.size+player2._2.size) {
            if(player1._2.size > player2._2.size){
              player1._2.size += .25*player2._2.size
              player2._2.size = startSize
              player2._2.location.x = level.startingLocation.x
              player2._2.location.y = level.startingLocation.y
              //respawn(player2._2)
            }
            else if(player2._2.size > player1._2.size){
              player2._2.size += .25*player1._2.size
              player1._2.size = startSize
              player1._2.location.x = nextDouble*level.gridWidth
              player1._2.location.y = nextDouble*level.gridHeight
              //respawn(player1._2)
            }
          }
        }
      }
    }
  }

  def respawn(player1: Player): Unit = {
    var flag: Boolean = true
    while(flag){
      var count = 0
      for(player2 <- players){
        if(player1.location != player2._2.location){
          val dist = calcDist(player1.location, player2._2.location)
          if(dist > player2._2.size){
            count += 1
            println(dist, player2._2.size, count)
          }
        }
      }
      if(count == players.size-1){
        println("CLEAR")
        flag = false
      }
    }
  }

  def calcDist(v1: PhysicsVector, v2: PhysicsVector): Double = {
    val x1 = v1.x
    val y1 = v1.y
    val x2 = v2.x
    val y2 = v2.y
    Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2))
  }

  def update(): Unit = {
    val time: Long = System.nanoTime()
    val dt = (time - this.lastUpdateTime) / 1000000000.0
    Physics.updateWorld(this.world, dt)
    eatPlayer()
    makeFood()
    eatFood()
    endGame()
    this.lastUpdateTime = time
  }

  def gameState(): String = {

    val gameState: Map[String, JsValue] = Map(
      "gridSize" -> Json.toJson(Map("x" -> level.gridWidth, "y" -> level.gridHeight)),
      "food" -> Json.toJson(this.food.map({ case (k, v) => Json.toJson(Map(
        "x" -> Json.toJson(v.location.x),
        "y" -> Json.toJson(v.location.y),
        "name" -> Json.toJson(k)
      )) })),
      "players" -> Json.toJson(this.players.map({ case (k, v) => Json.toJson(Map(
        "x" -> Json.toJson(v.location.x),
        "y" -> Json.toJson(v.location.y),
        "v_x" -> Json.toJson(v.velocity.x),
        "v_y" -> Json.toJson(v.velocity.y),
        "id" -> Json.toJson(k),
        "size" -> Json.toJson(v.size)
      )) })),
    )
    Json.stringify(Json.toJson(gameState))
  }

}
