import CassandraProbeService.{Reset, Retry}
import akka.actor.ActorSystem

import scala.concurrent.Await
import scala.concurrent.duration._

object Main extends App with CassandraProbeService {

  implicit val system = ActorSystem("system")

  implicit val ec = system.dispatcher

  //system.actorOf(Props[CassandraProbe])


  var retryState = Retry(7)

  val cancellable = system.scheduler.schedule(3 seconds, 2 seconds) {

    probe(retryState).onSuccess {
      case Retry(0)=>
        sys.exit(1)
      case Reset =>
        sys.exit(0)
      case retry: Retry =>
        retryState = retry
    }
  }

  sys.addShutdownHook {
    cancellable.cancel()
    Await.result(system.terminate(),Duration.Inf)
  }

}
