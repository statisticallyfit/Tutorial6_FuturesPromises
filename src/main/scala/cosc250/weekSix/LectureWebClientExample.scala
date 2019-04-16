package cosc250.weekSix

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import play.api.libs.ws.WSResponse
import play.api.libs.ws.ahc.AhcWSClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import scala.util.{Success, Try}

/**
  *
  */
object Prelim extends App {
	val promise = Promise[Int]
	val future = promise.future
	future.onSuccess({case x => println(x) })

	//later
	promise.success(123)

	try {
		Thread.sleep(1000)
	} catch {
		case _ => //
	}
}



object WebServerExample extends App {

	//need the akka threads for client querying
	implicit val system = ActorSystem("Sys")
	implicit val materializer = ActorMaterializer() //need these for wsClient arg implicit
	val wsClient: AhcWSClient = AhcWSClient()

	//note: won't wait for google to respond
	val futureResponse: Future[WSResponse] = wsClient.url("https://api.github.com/zen").get()
	val futureWords: Future[String] = futureResponse.map({response => response.body.toUpperCase})

	futureWords.foreach(println)
	println("This statement here prints immediately before request. ")


	try {
		Thread.sleep(1000)
		println("DONE HERE")
	} catch {
		case _ => //
	}
	/*
	//val future2: Future[WSResponse] = wsClient.url("https://api.github.com/zen")
	val futureResponse2: Future[WSResponse] = wsClient
		.url("https://api.github.com/zen").get().map(_.body).map(_.toUpperCase)*/

}




object WebServerFlatMap extends App {
	implicit val system = ActorSystem("Sys")
	implicit val materializer = ActorMaterializer() //need these for wsClient arg implicit
	val wsClient: AhcWSClient = AhcWSClient()


	def triteSaying(): Future[String] = {
		wsClient.url("https://api.github.com/zen")
     		.get()
     		.map(_.body)
	}
	/*
	try {
		Thread.sleep(1000)
		println("DONE HERE")
	} catch {
		case _ => //
	}*/

	/*val f = triteSaying().flatMap({ first =>
		triteSaying().map({second =>
			s"$first, and then $second"
		})
	})
	f.foreach(println)*/

	val f: Future[String] = for {
		first <- triteSaying()
		second <- triteSaying()
	} yield s"$first, and then $second"

	f.foreach(println)
}



object PragmaticPurity extends App {

	/*implicit val system = ActorSystem("Sys")
	implicit val materializer = ActorMaterializer()*/

	def longRunningTask(): Future[Int] = ???

	/*val p1 = Promise[String]
	val p2 = Promise[String]*/

	//note: main note: code is not pure, it looks pure but underneath the future
	// mutates because at first it is not completed.
	val p = Promise[Int]
	val future = p.future
	println(s"Has it finished? ${future.isCompleted}")
	p.success(123)
	//println(p, future)
	println(s"Has it finished? ${future.isCompleted}")
	//println(p, future)

	try {
		Thread.sleep(1000)
		println("Woke up!")
	} catch {
		case _ => //
	}
}


object RecoverExample extends App{

	val a = Try {
		throw new RuntimeException("Kaboom!")
	}
	println(a)
	println(a.recoverWith({ case ex: Throwable => Success(ex.getMessage)}))
}


object RecoverWithExample extends App {
	def longRunningTask(): Future[String] = ???
	def fetchContent(url: String): Future[String] = ???

	for {
		url <- longRunningTask() recoverWith {case _ => Future.successful("http://google.com")}
		content <- fetchContent(url)
	} yield {
		content.length
	}
}