import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{MethodRejection, MissingQueryParamRejection, RejectionHandler}

object ImplicitRejectionHandlerExample extends App {
  implicit val system = ActorSystem("ImplicitRejectionHandler")
  implicit val materializer = ActorMaterializer()
  import system.dispatcher

  val simpleRoute =
    path("api" / "myEndpoint") {
      get {
        complete(StatusCodes.OK)
      } ~
      parameter('id) { _ =>
        complete(StatusCodes.OK)
      }
    }

  // Implicit Rejection Handler
  implicit val implicitCustomRejectionHandler: RejectionHandler = RejectionHandler.newBuilder()
    .handle {
      case m: MethodRejection =>
        println(s"I got a method rejection: $m")
        complete("Rejected method!")
    }
    .handle {
      case m: MissingQueryParamRejection =>
        println(s"I got a query param rejection: $m")
        complete("Rejected query param!")
    }
    .result()
  // Having an implicit rejection handler for a route is called sealing a route.

  // akka-http server
  Http().bindAndHandle(simpleRoute, "localhost", 8081)
}
