import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.StrictLogging
import scala.io.StdIn

object Main extends App with StrictLogging {
  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val route: Route =
    path("hello") {
      get {
        complete("Hello, World!")
      }
    }

  val bindingFuture = Http().newServerAt("localhost", 8080).bind(route)
  logger.info(s"Server online at http://localhost:8080/hello\nPress RETURN to stop...")
  StdIn.readLine()
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())
}