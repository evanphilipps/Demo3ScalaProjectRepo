package ChaosTag.model.game_objects

import ChaosTag.model.physics.PhysicsVector

class Player(inputLocation: PhysicsVector,
             inputVelocity: PhysicsVector) extends PhysicalObject(inputLocation, inputVelocity) {
  var size: Double = .5
  val speed: Double = 10.0 / (size+.5)

  def move(direction: PhysicsVector){
    val normalDirection = direction.normal2d()
    this.velocity = new PhysicsVector(normalDirection.x * speed, normalDirection.y * speed)
  }

  def stop(): Unit ={
    this.velocity = new PhysicsVector(0, 0)
  }

}
