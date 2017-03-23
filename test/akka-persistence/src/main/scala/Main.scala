import akka.actor.ActorSystem

import scala.concurrent.Await
import scala.concurrent.duration._

object Main extends App with CassandraProbe with RetryTerminationDecision {

  import ProbeService._

  implicit val system = ActorSystem("system")

  implicit val ec = system.dispatcher

  //system.actorOf(Props[CassandraProbeActor])

  var retryState = Retry(3)

  def updateRetryState(s: State): Unit = {
    s match {
      case r: Retry =>
        retryState = r
      case _ =>
    }
  }

  val cancellable = system.scheduler.schedule(3 seconds, 2 seconds) {

    probe(retryState).onSuccess {
      case state =>
        determine(state, RetryTerminationDecision.Decision(() => { sys.exit(0) }, () => { sys.exit(1) }, () => updateRetryState(state)))()
    }
  }

  sys.addShutdownHook {
    cancellable.cancel()
    Await.result(system.terminate(), Duration.Inf)
  }

}
