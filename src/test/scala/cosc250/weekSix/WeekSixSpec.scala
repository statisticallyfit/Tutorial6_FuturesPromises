package cosc250.weekSix

import org.scalatest._
import org.scalatest.concurrent.ScalaFutures

import scala.concurrent.{Future, Promise}
import scala.concurrent.ExecutionContext.Implicits.global


/**
  * This is a specification file for ScalaTest. It's a set of unit tests written in a way that's designed to be
  * read easily.
  */
class WeekSixSpec extends FlatSpec with Matchers with ScalaFutures {

	import Exercise._

	"completeMyPromise" should "complete a promise I give it" in {
		val p = Promise[Int]
		val f = p.future
		completeMyPromise(p)

		f.isCompleted should be (true)
	}

	"doubleMyFuture" should "double any Future I give it" in {
		val immediate = Future.successful(1)
		doubleMyFuture(immediate).futureValue should be (2)

		val p = Promise[Int]
		new Thread(new Runnable() {
			def run() = {
				// Wait 10ms
				try { Thread.sleep(50) } catch { case x:Throwable => /* woken up */ }
				p.success(5)
			}
		}).start()
		doubleMyFuture(p.future).futureValue should be (10)
	}


	"compareMyFutureStrings" should "identify common letters in two strings, case insensitively" in {
		compareMyFutureStrings(Future.successful("Armidale"), Future.successful("Armadale")).futureValue should be (7)
	}

	"nameFromJson" should "get the right name" in {
		nameFromJason() should be ("Watership Down")
	}

	"webExample" should "fetch the file" in {
		webExample().futureValue should be ("This is a test\n")
	}

	"secondName" should "fetch the file" in {
		secondName().futureValue should be ("Soggyship Down")
	}

	"nameCharactersInCommon" should "fetch the file" in {
		nameCharactersInCommon(
			"http://turing.une.edu.au/~cosc250/lectures/cosc250/second.json",
			"http://turing.une.edu.au/~cosc250/lectures/cosc250/watership.json"
		).futureValue should be (9)
	}
}
