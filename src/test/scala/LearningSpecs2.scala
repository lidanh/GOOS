//import org.specs2.execute.{Result, AsResult}
//import org.specs2.mutable._
//import org.specs2.specification.{AroundExample, BeforeExample, Scope}
//
///**
// * Created by lidan on 13/01/15.
// */
//class LearningSpecs2 extends Specification with BeforeExample with AroundExample {
//  def before = {
//    println("BEFORE")
//  }
//
//  override protected def around[T: AsResult](t: => T) = {
//    println("OPEN")
//    AsResult(t)
//  }
//
//  "The hello world string" should {
//    "contain 11 characters" in {
//      "Hello world" must have size(11)
//    }
//
//    "start with 'Hello'" in {
//      "Hello world" must startWith("Hello")
//    }
//
//    "end with 'world'" in {
//      "Hello world" must endWith("world")
//    }
//
//    "str inside scope" in new trees {
//      str must have size(11)
//    }
//
//    "str before and after" in new treesBeforeAfter {
//      "test" must_==("test")
//      println("--- inside test")
//    }
//  }
//
//  trait trees extends Scope {
//    val str = "Test String"
//  }
//
//  trait treesBeforeAfter extends trees with BeforeAfter {
//    def before() = {
//      println("-- Before test")
//    }
//
//    def after() = {
//      println("-- After test")
//    }
//  }
//}
