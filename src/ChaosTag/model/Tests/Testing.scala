package ChaosTag.model.Tests

import ChaosTag.model.Game
import ChaosTag.model.game_objects.Player
import ChaosTag.model.physics.PhysicsVector
import org.scalatest
import org.scalatest.FunSuite

class Testing extends FunSuite {
  test("Game works!"){
    var game = new Game()
    game.PlayerJoined("Elijah")
    game.PlayerJoined("Evan")

    var vector1 = new PhysicsVector(10, 12, 3)
    var vector2 = new PhysicsVector(24, 36, 90)

    game.players("Elijah").location.x = 7
    game.players("Elijah").location.y = 5
    game.players("Evan").location.x = 7
    game.players("Evan").location.y = 5

    game.eatPlayer()

    assert(game.players.head._2.size == game.startSize)
    assert(game.players.last._2.size == game.startSize)

    assert(game.players.head._2.location.x == 7)
    assert(game.players.head._2.location.y == 5)
    assert(game.players.last._2.location.x == 7)
    assert(game.players.last._2.location.y == 5)

    assert(game.players.size == 2)

    game.players.head._2.size = 3.0

    game.eatPlayer()

    assert(game.players.head._2.size == 3.125)
    assert(game.players.last._2.size == game.startSize)

    game.players("Evan").size = 200
    assert(game.players("Evan").size == 200)

    game.PlayerLeft("Evan")

    assert(game.players.size == 1)

    assert(game.food.size == 1)

    game.PlayerJoined("Evan")

    assert(game.players("Evan").size == game.startSize)
    game.players("Evan").location.x = 15
    game.players("Evan").location.y = 15
    game.food.head._2.location.x = 7
    game.food.head._2.location.y = 5

    game.eatFood()

    assert(game.food.isEmpty)
    assert(game.players("Elijah").size == 3.225)

    game.makeFood()

    assert(game.food.size == 20)

    assert(game.calcDist(vector1, vector2) == 27.784887978899608)
  }
}

