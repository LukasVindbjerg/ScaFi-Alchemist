import it.unibo.alchemist.model.implementations.positions.LatLongPosition
import it.unibo.alchemist.model.scafi.ScafiIncarnationForAlchemist._

import scala.util.Try

class prova extends AggregateProgram with StandardSensors with ScafiAlchemistSupport {
  val sourceID = 0

  override def main(): Any = {
    val isSource = mid == sourceID

    node.put("distance", alchemistEnvironment.getDistanceBetweenNodes(alchemistEnvironment.getNodeByID(mid), alchemistEnvironment.getNodeByID(sourceID)))
    node.put("gradient", gradient(isSource, nbrRange))

    val timeToGo = Try{ node.get[Double]("timeToGo") }.getOrElse(200 * nextRandom())
    node.put("timeToGo", timeToGo)

    if(Try { node.get[(Double,Double)]("target") }.isSuccess || timestamp() < timeToGo){
      // Do nothing
    } else {
      val x = 8 * nextRandom()-4
      val y = 4 * nextRandom()-2
      val llpos = new LatLongPosition(x,y)
      node.put("target", List(x,y))
    }

    0
  }

  def gradient(isSource: Boolean, metric: => Double): Unit = {
    rep(Double.PositiveInfinity) { distance =>
      mux(isSource){ 0.0 }{ minHood{ nbr(distance) + metric } }
    }
  }
}
