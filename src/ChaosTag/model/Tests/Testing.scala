package ChaosTag.model.Tests

import ChaosTag.model.Game
import ChaosTag.model.game_objects.Player
import ChaosTag.model.physics.PhysicsVector
import org.scalatest
import org.scalatest.FunSuite

class Testing extends FunSuite {
  test("Game works!"){
    var game = new Game()
    game.addPlayer("Elijah")
    game.addPlayer("Evan")
    game.eatPlayer()
    assert(game.players.head._2.size == game.startSize)
    assert(game.players.last._2.size == game.startSize)
    assert(game.players.size == 2)
    game.players.head._2.size = 50.0
    game.players.head._2.location.x = 2.0
    game.players.head._2.location.y = 2.0
    game.players.last._2.location.x = 2.0
    game.players.last._2.location.y = 2.0
    game.eatPlayer()
    assert(game.players.head._2.size == 50.125)
    assert(game.players.last._2.size == game.startSize)
  }
}

