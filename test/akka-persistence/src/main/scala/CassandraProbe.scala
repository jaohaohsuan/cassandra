import akka.actor.Actor

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

/**
  * Created by henry on 3/22/17.
  */


class CassandraProbe extends Actor with CassandraProbeService {

  import CassandraProbeService._
  import context._

  private implicit val ec: ExecutionContext = system.dispatcher

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

    case Retry(t) if t <= 0 =>
      cancellable.cancel()
      system.terminate()

    case Reset =>
      retryState = reset

    case state: Retry =>
      retryState = state
  }
}
