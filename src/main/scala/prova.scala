package prova

import it.unibo.alchemist.model.scafi.ScafiIncarnationForAlchemist._
import scala.math._


class prova extends AggregateProgram with FieldUtils with StandardSensors with ScafiAlchemistSupport {


  // Crea una coppia
  def pair[A,B](x : A, y : B) : Tuple2[A,B] = {
    Tuple2(x,y)
  }

  
  // Primo elemento di una coppia
  def fst[A,B](t : Tuple2[A,B]) : A = {
    t._1
  }

  
  // Secondo elemento di una coppia
  def snd[A,B](t : Tuple2[A,B]) : B = {
    t._2
  }

  
  // Coordinate correnti come lista di due Double
  def getCoordinates(): List[Double] = {
    alchemistEnvironment.getPosition(alchemistEnvironment.getNodeByID(mid)).getCartesianCoordinates.toList
  }

  
  // Conta i round che passano
  def count() : Int = {
    rep(0) { n => n+1 }
  }

  
  // Vale sempre il primo valore mai assunto dall'argomento.
  def constant[T](value: => T): T = {
    rep(value) { old => old }
  }

  
  // Calcolo di distanze approssimato (gradiente).
  // isSource: se il device corrente e' una sorgente
  // metric:   funzione che calcola la distanza dispetto ai vicini del device corrente
  // returns:  la lunghezza del cammino minimo nella rete verso una sorgente
  def gradient(isSource: Boolean, metric: => Double): Double = {
    rep(Double.PositiveInfinity) { distance =>
      mux(isSource){
        // le sorgenti hanno distanza 0
        0.0
      }{
        // gli altri il minimo dei valori dei vicini sommati alla loro distanza relativa
        minHood{ nbr{distance} + metric }
      }
    }
  }

  
  override def main(): Any = {
    node.put("language", "scafi")

    // la sorgente e' il nodo 0 (fermo nel mezzo)
    val sourceID = 0
    val isSource = mid == sourceID

    // salvo la stima di distanza e quella esatta
    node.put("distance", alchemistEnvironment.getDistanceBetweenNodes(alchemistEnvironment.getNodeByID(mid), alchemistEnvironment.getNodeByID(sourceID))) // distanza euclidea esatta
    node.put("gradient", gradient(isSource, nbrRange)) // stima tramite gradiente

    // determino un tempo in cui iniziare a muovermi a caso tra 0 e 200
    val timeToGo = constant(200 * nextRandom())
    node.put("timeToGo", timeToGo)

    // determino un luogo in cui muovermi, se e' il momento di farlo
    branch(timestamp() < timeToGo){
      // non e' ancora ora, e non faccio nulla
    } {
      // scelgo un obiettivo a caso tra [-4,-2] e [4,2]
      val target = constant(List(8*nextRandom()-4, 4*nextRandom()-2))
      node.put("target", target)
    }

    // insert your code here
  }

  
  // Massimo tra due elementi
  def max[T](x : T, y : T)(implicit ord: Ordering[T]) : T = {
    ord.max(x, y)
  }

  // Somma di un campo di numeri
  def sumHood[T](x : => T)(implicit numEv: Numeric[T]) : T = {
    includingSelf.sumHood(x)
  }

  // Moltiplicazione per scalare di coordinate
  def *(x : Double, y : List[Double]) : List[Double] = {
    y.map(_*x)
  }

  // Moltiplicazione per scalare di coordinate
  def *(x : List[Double], y : Double) : List[Double] = {
    x.map(_*y)
  }

  // Moltiplicazione punto-a-punto di coordinate
  def *(x : List[Double], y : List[Double]) : List[Double] = {
    assert(x.length == y.length, "impossibile moltiplicare vettorialmente vettori di lunghezze differenti")
    (x,y).zipped.map(_*_)
  }

  // Somma con scalare di coordinate
  def +(x : Double, y : List[Double]) : List[Double] = {
    y.map(_+x)
  }

  // Somma con scalare di coordinate
  def +(x : List[Double], y : Double) : List[Double] = {
    x.map(_+y)
  }

  // Somma punto-a-punto di coordinate
  def +(x : List[Double], y : List[Double]) : List[Double] = {
    assert(x.length == y.length, "impossibile sommare vettorialmente vettori di lunghezze differenti")
    (x,y).zipped.map(_+_)
  }

  // Inversione punto-a-punto di coordinate
  def -(x : List[Double]) : List[Double] = {
    x.map(-_)
  }

  // Sottrazione con scalare di coordinate
  def -(x : List[Double], y : Double) : List[Double] = {
    x.map(_-y)
  }

  // Sottrazione punto-a-punto di coordinate
  def -(x : List[Double], y : List[Double]) : List[Double] = {
    assert(x.length == y.length, "impossibile sottrarre vettorialmente vettori di lunghezze differenti")
    (x,y).zipped.map(_-_)
  }
}
