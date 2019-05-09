package ChaosTag.model

import scala.util.Random.nextInt

object Level {

  def apply(number: Int): Level = {
    new Level
  }

}

class Level {

  var gridWidth: Int = 75
  var gridHeight: Int = 40

  var startingLocation = new GridLocation(nextInt(gridWidth - 2)+1, nextInt(gridHeight - 2)+1)

}
