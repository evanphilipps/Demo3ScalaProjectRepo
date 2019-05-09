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
    game.PlayerLeft("Evan")

    game.players("Elijah").location.x = 5
    game.players("Elijah").location.y = 5
    game.players("Evan").location.x = 7
    game.players("Evan").location.y = 5

    game.eatPlayer()

    assert(game.players.head._2.size == game.startSize)
    assert(game.players.last._2.size == game.startSize)

    assert(game.players.head._2.location.x == 5)
    assert(game.players.head._2.location.y == 5)
    assert(game.players.last._2.location.x == 7)
    assert(game.players.last._2.location.y == 5)

    assert(game.players.size == 2)

    game.players.head._2.size = 3.0

    game.eatPlayer()

    assert(game.players.head._2.size == 3.125)
    assert(game.players.last._2.size == game.startSize)
  }
}

