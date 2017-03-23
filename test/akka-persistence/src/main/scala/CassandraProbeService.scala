
import akka.actor.ActorSystem
import akka.persistence.cassandra.query.scaladsl.CassandraReadJournal
import akka.persistence.query.PersistenceQuery

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by henry on 3/22/17.
  */

object CassandraProbeService {

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

trait CassandraProbeService {

  import CassandraProbeService._

  def probe(state: Retry)(implicit system: ActorSystem, ec: ExecutionContext): Future[State] = {

    val session = PersistenceQuery(system).readJournalFor[CassandraReadJournal](CassandraReadJournal.Identifier).session

    session.underlying().map { _ => session.close(); Reset } recover { case _ => Retry.decrease(state) }
  }
}
