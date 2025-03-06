package example

import it.unibo.alchemist.model.scafi.ScafiIncarnationForAlchemist._
// import it.unibo.scafi.incarnations.basicabstractincarnation
import scala.math._

// object myincarnation extends basicabstractincarnation

// import example.myincarnation._

class example extends AggregateProgram with FieldUtils with StandardSensors with ScafiAlchemistSupport {


  // builds a pair
  def pair[A,B](x : A, y : B) : Tuple2[A,B] = {
    Tuple2(x,y)
  }


  // first element of a pair
  def fst[A,B](t : Tuple2[A,B]) : A = {
    t._1
  }


  // second element of a pair
  def snd[A,B](t : Tuple2[A,B]) : B = {
    t._2
  }


  // device coordinates
  def getCoordinates(): List[Double] = {
    alchemistEnvironment.getPosition(alchemistEnvironment.getNodeByID(mid)).getCartesianCoordinates.toList
  }


  // counts elapsed rounds
  def elapsed() : Int = {
    rep(0) { n => n+1 }
  }


  // it preserves the value passed in the first round across following rounds
  def constant[T](value: => T): T = {
    rep(value) { old => old }
  }


  // computes approximated distances (ABF algorithm).
  // isSource: whether the current device is a source
  // metric:   function returning distances to neighbours
  // returns:  length of the shortest path towards a source
  def ABF(isSource: Boolean, metric: => Double): Double = {
    // distance starts from infinity
    rep(Double.PositiveInfinity) { distance =>
      mux(isSource){
        // sources have distance zero
        0.0
      }{
        // others have the minimum across neighbours of their distance to a source plus the relative distance
        minHood{ nbr{distance} + metric }
      }
    }
  }

  // Get neighbours by ID
  def getNbrs() : Int = {
    rep(0) {nbrs =>
      foldhoodPlus(Set.empty[ID])(_++_)(Set(nbr(mid()))).size
    }
  }

  def getNbrCount() : Int = {
    // Just go through each nbr and increment counter
    foldhoodPlus(0)(_ + _)(1)
  }

  def maxNbrsSeen() : Int = {
    rep(0) { maxNbrCount =>
      mux(getNbrCount() > maxNbrCount)(getNbrCount())(maxNbrCount)
    }
  }

  def maxNbrsInNetwork(): Int = {
    rep(0) { maxNbrInNetwork =>
      Math.max(maxNbrsSeen(), foldhood(0)(Math.max)(nbr{maxNbrInNetwork}))
    }
  }

  def mostLonelyNeighbor(): (ID, List[Double]) = {
    foldhood(pair(mid(), getCoordinates()))
            ((p1, p2) => if (nbr(getNbrCount()) < getNbrCount()) p1 else p2)
            (pair(nbr(mid()), nbr(getCoordinates())))
  }




  override def main(): Any = {
    node.put("language", "scafi")

    // source is node 0 (standing in the middle)
    val sourceID = 0
    val isSource = mid == sourceID

    // export correct and estimated distances into the simulator
    node.put("distance", alchemistEnvironment.getDistanceBetweenNodes(alchemistEnvironment.getNodeByID(mid), alchemistEnvironment.getNodeByID(sourceID))) // exact euclidean distance
    node.put("abf", ABF(isSource, nbrRange)) // shortest-path estimated distance

    // choose a random time between 0 and 200 in which to start moving
    val timeToGo = constant(200 * nextRandom())
    node.put("timeToGo", timeToGo) // memorizing it

    // choose a place where to move, if it's time to do it
    branch(timestamp() < timeToGo){
      // not time yet, doing nothing
    } {
      // select a random target between [-4,-2] and [4,2]
      val target = constant(List(8*nextRandom()-4, 4*nextRandom()-2))
      node.put("target", target)
    }


    // insert your code here!
    // E1: Get nbr count
    node.put("Nbrs", getNbrCount())

    // E2: Get max nbr count
    node.put("Max Nbrs", maxNbrsSeen())

    // E3: Total max nbrs seen in network:
    node.put("GlobalMaxNbrs", maxNbrsInNetwork())

    node.put("Lonliest Nbr", mostLonelyNeighbor())
  }

  
  // maximum between two elements
  def max[T](x : T, y : T)(implicit ord: Ordering[T]) : T = {
    ord.max(x, y)
  }

  // sum of a neighbouring field of numbers
  def sumHood[T](x : => T)(implicit numEv: Numeric[T]) : T = {
    includingSelf.sumHood(x)
  }

  // pointwise operations on lists
  implicit class PointwiseDoubleList(lst: List[Double]){
      def +(v: Double): List[Double] = lst.map(_+v)
      def +(other: List[Double]): List[Double] = lst.zip(other).map(v => v._1+v._2).toList
      def -(v: Double): List[Double] = lst.map(_-v)
      def -(other: List[Double]): List[Double] = lst.zip(other).map(v => v._1-v._2).toList
      def *(v: Double): List[Double] = lst.map(_*v)
      def *(other: List[Double]): List[Double] = lst.zip(other).map(v => v._1*v._2).toList
      def /(v: Double): List[Double] = lst.map(_/v)
      def /(other: List[Double]): List[Double] = lst.zip(other).map(v => v._1/v._2).toList
  }
}
