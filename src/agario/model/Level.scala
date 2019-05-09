package agario.model

import scala.util.Random.nextInt

object Level {

  def apply(number: Int): Level = {
    new Level
  }

}

class Level {

  var gridWidth: Int = 30
  var gridHeight: Int = 30

  var startingLocation = new GridLocation(nextInt(gridWidth - 1), nextInt(gridHeight - 1))

}
