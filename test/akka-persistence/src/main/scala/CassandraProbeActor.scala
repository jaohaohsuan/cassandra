import akka.actor.{Actor, ActorSystem}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

/**
  * Created by henry on 3/22/17.
  */


class CassandraProbeActor extends Actor with CassandraProbe {

  import ProbeService._

  implicit val system: ActorSystem = context.system
  implicit val ec: ExecutionContext = system.dispatcher

  private val reset = Retry(20)
  private var retryState = reset

  private val cancellable = system.scheduler.schedule(3 seconds, 2 seconds, self, "Probe")

  override def receive: Receive = {

    case "Probe" =>
      probe(retryState).onSuccess {
        case m =>
          self ! m
          println(s"$m")
      }

    case Retry(0) =>
      cancellable.cancel()
      system.terminate()

    case Reset =>
      retryState = reset

    case state: Retry =>
      retryState = state
  }
}
