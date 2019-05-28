import it.unibo.alchemist.model.implementations.positions.LatLongPosition
import it.unibo.alchemist.model.scafi.ScafiIncarnationForAlchemist._

import scala.util.Try

class prova extends AggregateProgram with StandardSensors with ScafiAlchemistSupport {

  // Vale sempre il primo valore mai assunto dall'argomento.
  def constant[T](value: => T) : T = {
    rep(value) { old => old }
  }

  // Calcolo di distanze approssimato (gradiente).
  // isSource: se il device corrente e' una sorgente
  // metric:   funzione che calcola la distanza dispetto ai vicini del device corrente
  // returns:  la lunghezza del cammino minimo nella rete verso una sorgente
  def gradient(isSource: Boolean, metric: => Double): Double = {
    rep(Double.PositiveInfinity) { distance =>
      mux(isSource){ 0.0 }{ minHood{ nbr(distance) + metric } }
    }
  }

  override def main(): Any = {
    node.put("language", "scafi")

    // la sorgente e' il nodo 0 (fermo nel mezzo)
    val sourceID = 0
    val isSource = mid == sourceID

    // salvo la stima di distanza e quella esatta
    node.put("distance", alchemistEnvironment.getDistanceBetweenNodes(alchemistEnvironment.getNodeByID(mid), alchemistEnvironment.getNodeByID(sourceID))) // esatta
    node.put("gradient", gradient(isSource, nbrRange)) // stima

    // determino un tempo in cui iniziare a muovermi a caso tra 0 e 200
    val timeToGo = constant(200 * nextRandom())
    node.put("timeToGo", timeToGo)

    // determino un luogo in cui muovermi, se e' il momento di farlo
    if(timestamp() < timeToGo){
      // non e' ancora ora, e non faccio nulla
    } else {
      // scelgo un obiettivo a caso tra [-4,-2] e [4,2]
      val target = constant(List(8*nextRandom()-4, 4*nextRandom()-2))
      node.put("target", target)
    }
    node.put("cose", getCoordinates())

    0
  }

  def getCoordinates(): List[Double] = {
    alchemistEnvironment.getPosition(alchemistEnvironment.getNodeByID(mid)).getCartesianCoordinates.toList
  }
}
