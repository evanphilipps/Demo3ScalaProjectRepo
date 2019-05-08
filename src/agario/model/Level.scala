package agario.model

import scala.util.Random.nextInt

object Level {

  def apply(number: Int): Level = {
    new Level
  }

}

class Level {

  var gridWidth: Int = 100
  var gridHeight: Int = 100

  var startingLocation = new GridLocation(nextInt(gridWidth - 1), nextInt(gridHeight - 1))

}
