package scala.collection.immutable

import org.junit.Assert.assertSame
import org.junit.{Assert, Test}
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import scala.ref.WeakReference

@RunWith(classOf[JUnit4])
class ListTest {
  
  /**
   * REMOVED FROM in favor of collect!
   * @see Adligo.Scala.LIB?.A 
   */
  @Test
  def testConvert(): Unit = {
    val fruit : List[Fruit] = CollectionOfFruit.allFruit
    val ids : List[Int] = fruit.convert( f => {
      if (f.getSpecies() == CollectionOfFruit.apple) {
        (true, f.getRfid())
      } else {
        (false, 0)//note the zero doesn't matter, 
        //it could be changed to an optional, but this seems faster
      }
    })
    //ids.foreach( id => println(id))
    Assert.assertTrue(ids.contains(1))
    Assert.assertTrue(ids.contains(2))
    Assert.assertTrue(ids.contains(3))
    Assert.assertTrue(ids.size == 3)
    
  }
  
    /**
   * Adligo.Scala.LIB?.A combines a map and filter operation into a single transversal
   * instead of two.
   */
  @Test
  def testGroupCollect(): Unit = {
    val fruit : List[Fruit] = CollectionOfFruit.allFruit
    val applesAndPears = List(CollectionOfFruit.apple, CollectionOfFruit.pear)
    
    //find large fruit
    val fruitMap : Map[String, List[Int]] = fruit.groupCollect( 
        g => g.getSpecies()
        ) {
          case f if (f.getWeight() > 2.0 ) => f.getRfid()
        }
    val appleIds = fruitMap.get(CollectionOfFruit.apple).head
    //appleIds.foreach( id => println(id))
    Assert.assertTrue(appleIds.contains(2))
    Assert.assertTrue(appleIds.contains(3))
    Assert.assertTrue("" + appleIds, appleIds.size == 2)
    
    val bananaIds = fruitMap.get(CollectionOfFruit.banana).head
    Assert.assertTrue(bananaIds.contains(4))
    Assert.assertTrue(bananaIds.contains(5))
    Assert.assertTrue(bananaIds.size == 2)
    
    val pearIds = fruitMap.get(CollectionOfFruit.pear).head
    Assert.assertTrue(pearIds.contains(7))
    Assert.assertTrue(pearIds.contains(8))
    Assert.assertTrue(pearIds.size == 2)
    Assert.assertTrue(fruitMap.size == 3)
    
  }
  
       /**
   * Adligo.Scala.LIB?.A combines a map and filter operation into a single transversal
   * instead of two.
   */
  @Test
  def testGroupCollectReduce(): Unit = {
    val fruit : List[Fruit] = CollectionOfFruit.allFruit
    val applesAndPears = List(CollectionOfFruit.apple, CollectionOfFruit.pear)
    
    //sum large fruit
    val friutMap : Map[String, Double] = fruit.groupCollectReduce( g => g.getSpecies()) {
      case f if f.getWeight() > 2.0 => f.getWeight()
     }( _ + _)
    
    //friutMap.foreach( id => println(id))
    
    
    def eq(a: Double, b: Double): Boolean = {
      val diff1 = Math.abs(a -  b)
      if (-0.00001 <= diff1 && diff1 <= 0.000001) true else false
    }
    
    val heavyApples = friutMap.get(CollectionOfFruit.apple)
    Assert.assertTrue("'" + heavyApples.get + "'", eq(7.5, heavyApples.get))
    
    val heavyBananas = friutMap.get(CollectionOfFruit.banana)
    Assert.assertTrue("'" + heavyBananas.get + "'", eq(6.2, heavyBananas.get))
    
    val heavyPears = friutMap.get(CollectionOfFruit.pear)
    Assert.assertTrue("'" + heavyPears.get + "'", eq(4.5, heavyPears.get))
    
    Assert.assertTrue(friutMap.size == 3)
    
  }
  
    /**
   * Adligo.Scala.LIB?.A combines a map and filter operation into a single transversal
   * instead of two.
   */
  @Test
  def testRefineCollect(): Unit = {
    val fruit : List[Fruit] = CollectionOfFruit.allFruit
    val applesAndPears = List(CollectionOfFruit.apple, CollectionOfFruit.pear)
    
    //find large fruit
    val heavyApplesAndPears : Map[String, List[Int]] = fruit.refineCollect{
      case x if applesAndPears.contains(x.getSpecies()) => x.getSpecies()
    }{
      case y if y.getWeight()  > 2.0 => y.getRfid()
    }
        
    val appleIds = heavyApplesAndPears.get(CollectionOfFruit.apple).head
    //appleIds.foreach( id => println(id))
    Assert.assertTrue(appleIds.contains(2))
    Assert.assertTrue(appleIds.contains(3))
    Assert.assertTrue(appleIds.size == 2)
    
    val pearIds = heavyApplesAndPears.get(CollectionOfFruit.pear).head
    Assert.assertTrue(pearIds.contains(7))
    Assert.assertTrue(pearIds.contains(8))
    Assert.assertTrue(pearIds.size == 2)
    Assert.assertTrue(heavyApplesAndPears.size == 2)
    
  }
  
      /**
   * Adligo.Scala.LIB?.A combines a map and filter operation into a single transversal
   * instead of two.
   */
  @Test
  def testRefineCollectReduce(): Unit = {
    val fruit : List[Fruit] = CollectionOfFruit.allFruit
    val applesAndPears = List(CollectionOfFruit.apple, CollectionOfFruit.pear)
    
    //sum large fruit
    val heavyApplesAndPears : Map[String, Double] = fruit.refineCollectReduce {
       case x if applesAndPears.contains(x.getSpecies()) => x.getSpecies()
    }{
      case y if y.getWeight()  > 2.0 => y.getWeight()
    }( _ + _)
    
    heavyApplesAndPears.foreach( id => println(id))
    val heavyApples = heavyApplesAndPears.get(CollectionOfFruit.apple)
    
    def eq(a: Double, b: Double): Boolean = {
      val diff1 = Math.abs(a -  b)
      if (-0.00001 <= diff1 && diff1 <= 0.000001) true else false
    }
    
    Assert.assertTrue("'" + heavyApples.get + "'", eq(7.5, heavyApples.get))
    
    val heavyPears = heavyApplesAndPears.get(CollectionOfFruit.pear)
    Assert.assertTrue("'" + heavyPears.get + "'", eq(4.5, heavyPears.get))
    Assert.assertTrue(heavyApplesAndPears.size == 2)
    
  }
  
  
    /**
   * Adligo.Scala.LIB?.A combines a map and filter operation into a single transversal
   * instead of two.
   */
  @Test
  def testRefineMap(): Unit = {
    val fruit : List[Fruit] = CollectionOfFruit.allFruit
    val applesAndPears = List(CollectionOfFruit.apple, CollectionOfFruit.pear)
    
    //find large fruit
    val heavyApplesAndPears : Map[String, List[Int]] = fruit.refineMap{
      case g if applesAndPears.contains(g.getSpecies()) => g.getSpecies()
    }( m => m.getRfid())
        
    val appleIds = heavyApplesAndPears.get(CollectionOfFruit.apple).head
    //appleIds.foreach( id => println(id))
    Assert.assertTrue(appleIds.contains(1))
    Assert.assertTrue(appleIds.contains(2))
    Assert.assertTrue(appleIds.contains(3))
    Assert.assertTrue(appleIds.size == 3)
    
    val pearIds = heavyApplesAndPears.get(CollectionOfFruit.pear).head
    Assert.assertTrue(pearIds.contains(6))
    Assert.assertTrue(pearIds.contains(7))
    Assert.assertTrue(pearIds.contains(8))
    Assert.assertTrue(pearIds.size == 3)
    Assert.assertTrue(heavyApplesAndPears.size == 2)
    
  }
  
      /**
   * Adligo.Scala.LIB?.A combines a map and filter operation into a single transversal
   * instead of two.
   */
  @Test
  def testRefineMapReduce(): Unit = {
    val fruit : List[Fruit] = CollectionOfFruit.allFruit
    val applesAndPears = List(CollectionOfFruit.apple, CollectionOfFruit.pear)
    
    
    //sum large fruit
    val fruitMap : Map[String, Double] = fruit.refineMapReduce{
      case g if applesAndPears.contains(g.getSpecies()) => g.getSpecies()
    }( f => f.getWeight())( _ + _)
    
    //heavyApplesAndPears.foreach( id => println(id))
    
    
    def eq(a: Double, b: Double): Boolean = {
      val diff1 = Math.abs(a -  b)
      if (-0.00001 <= diff1 && diff1 <= 0.000001) true else false
    }
    
    val apples = fruitMap.get(CollectionOfFruit.apple)
    Assert.assertTrue("'" + apples.get + "'", eq(8.6, apples.get))
    
    
    val pears = fruitMap.get(CollectionOfFruit.pear)
    Assert.assertTrue("'" + pears.get + "'", eq(5.6, pears.get))
    Assert.assertTrue(fruitMap.size == 2)
    
  }
  
  /**
   * Test that empty iterator does not hold reference
   * to complete List
   */
  @Test
  def testIteratorGC(): Unit = {
    var num = 0
    var emptyIterators = Seq.empty[(Iterator[Int], WeakReference[List[Int]])]

    do {
      val list = List.fill(10000)(num)
      val ref = WeakReference(list)

      val i = list.iterator

      while (i.hasNext) i.next()

      emptyIterators = (i, ref) +: emptyIterators

      num+=1
    } while (emptyIterators.forall(_._2.get.isDefined) && num<1000)

    // check something is result to protect from JIT optimizations
    for ((i, _) <- emptyIterators) {
      Assert.assertTrue(i.isEmpty)
    }

    // await gc up to ~5 seconds
    var forceLoops = 50
    while (emptyIterators.forall(_._2.get.isDefined) && forceLoops>0) {
      System.gc()
      Thread.sleep(100)
      forceLoops -= 1
    }

    // real assertion
    Assert.assertTrue(emptyIterators.exists(_._2.get.isEmpty))
  }

  @Test
  def updated(): Unit = {
    val xs = 1 :: 2 :: Nil
    Assert.assertEquals(0 :: 2 :: Nil, xs.updated(index = 0, elem = 0))
    Assert.assertEquals(1 :: 0 :: Nil, xs.updated(index = 1, elem = 0))
    try {
      xs.updated(index = -1, 0)
      Assert.fail("No exception thrown")
    } catch {
      case e: IndexOutOfBoundsException => ()
    }
    try {
      xs.updated(index = 2, 0)
      Assert.fail("No exception thrown")
    } catch {
      case e: IndexOutOfBoundsException => ()
    }
  }

  @Test
  def factoryReuse(): Unit = {
    val ls = List("a")
    assertSame(ls, List.apply(ls: _*))
    assertSame(ls, List.from(ls))
  }

  @Test def checkSearch: Unit = SeqTests.checkSearch(List(0 to 1000: _*), 15,  implicitly[Ordering[Int]])
}
