import akka.actor.{Actor, ActorSystem}

import scala.concurrent.{Await, ExecutionContext}
import scala.concurrent.duration._
import akka.pattern._
import akka.persistence.cassandra.query.scaladsl.CassandraReadJournal
import akka.persistence.query.PersistenceQuery
import com.typesafe.scalalogging.LazyLogging

class CassandraProbeActor extends Actor with LazyLogging with ProbeService[com.datastax.driver.core.Session] with RetryDecision {

  import ProbeService._

  implicit val system: ActorSystem = context.system
  implicit val ec: ExecutionContext = system.dispatcher

  private val reset = Retry(5)
  private var retryState = reset

  private val cancellable = system.scheduler.schedule(3 seconds, 2 seconds, self, "Probe")

  private val retryDecision =  Decision(passed, exit, continue)

  def exit(): Unit = {
    cancellable.cancel()
    system.terminate()
  }

  def passed(): Unit = {
    retryState = reset
    logger.info("Cassandra up and running.")
  }

  def continue(retry: Retry ): Unit = retryState = retry

  override def receive: Receive = {

    case "Probe" =>
      val session = PersistenceQuery(system).readJournalFor[CassandraReadJournal](CassandraReadJournal.Identifier).session
      // close session after connected
      probe(session.underlying().map { s => session.close(); s }, retryState) pipeTo self

    case state: State =>
      determine(state, retryDecision)()

  }
}
