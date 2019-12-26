package scala.collection.immutable

import org.junit.Assert.assertSame
import org.junit.{Assert, Test}
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Adligo.SIP?.A combines a map and filter operation into a single transversal
 * instead of two.
 * 
 * @param ripeness 0-10 10 is ripe!
 * @param weight in oz
 */
class Fruit(species: String, rfid: Int, ripeness: Double, weight: Double) {
  def getSpecies() : String =  { species }
  def getRfid() : Int =  { rfid }
  def getRipeness() : Double =  { ripeness }
  def getWeight() : Double =  { weight }
}

object CollectionOfFruit {
  val apple = "apple"
  val banana = "banana"
  
  val allFruit = List[Fruit] (
      new Fruit(apple,1, 2.7, 1.1),
      new Fruit(apple,2, 3.1, 3.3),
      new Fruit(apple,3, 2.7, 4.2),
      new Fruit(banana,4, 2.7, 3.0),
      new Fruit(banana,5, 3.7, 3.2)
      )

}