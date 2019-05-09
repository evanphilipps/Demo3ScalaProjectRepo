package ChaosTag.model

import scala.util.Random.nextInt

object gameMap {

  def apply(number: Int): gameMap = {
    new gameMap
  }

}

class gameMap {

  var gridWidth: Int = 75
  var gridHeight: Int = 40

  var startingLocation = new XYLocation(nextInt(gridWidth - 2)+1, nextInt(gridHeight - 2)+1)

}
