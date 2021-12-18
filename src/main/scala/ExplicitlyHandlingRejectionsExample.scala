import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Rejection, RejectionHandler, Route}

object ExplicitlyHandlingRejectionsExample extends App {

  implicit val system = ActorSystem("ExplicitlyHandlingRejections")
  implicit val materializer = ActorMaterializer()
  import system.dispatcher

  /** Explicitly Defined Rejection Handlers
   * BadRequest Rejection Handler
   * Forbidden Rejection Handler
   */
  val badRequestHandler: RejectionHandler = { rejections: Seq[Rejection] =>
    println(s"I have encountered rejections: $rejections")
    Some(complete(StatusCodes.BadRequest))
  }

  val forbiddenHandler: RejectionHandler = { rejections: Seq[Rejection] =>
    println(s"I have encountered rejections: $rejections")
    Some(complete(StatusCodes.Forbidden))
  }

  val routeWithExplicitRejectionHandlers: Route =
    handleRejections(badRequestHandler) { // handle rejections from the top level
      // define server logic inside
      path("api" / "myEndpoint") {
        get {
          complete(StatusCodes.OK)
        } ~
        post {
          handleRejections(forbiddenHandler) { // handle rejections WITHIN
            parameter('myParam) { _ =>
              complete(StatusCodes.OK)
            }
          }
        }
      }
    }

  // akka-http server
  Http().bindAndHandle(routeWithExplicitRejectionHandlers, "localhost", 8080)
}
