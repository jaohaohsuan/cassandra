
import akka.actor.ActorSystem

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by henry on 3/22/17.
  */

object ProbeService {

  trait State
  object Retry {
    def decrease(retry: Retry): Retry = {
      val Retry(t) = retry
      val t2 = if (t > 0)
        t - 1
      else
        0

      Retry(t2)
    }
  }

  case class Retry(times: Int) extends State
  case object Reset extends State

}

trait ProbeService[T] {

  import ProbeService._

  implicit val ec: ExecutionContext

  def target: Future[T]

  def probe(state: Retry): Future[State] = {
    target.map { _ => Reset } recover { case _ => Retry.decrease(state) }
  }
}

object RetryTerminationDecision {
  case class Decision(passed: () => Unit, exit: () => Unit, continue: () => Unit)
}

trait RetryTerminationDecision {

  import ProbeService._

  def determine(state: State, decision: RetryTerminationDecision.Decision): () => Unit = {
    state match {
      case Reset => decision.passed
      case Retry(0) => decision.exit
      case _ => decision.continue
    }
  }
}