import akka.actor.ActorSystem
import akka.persistence.cassandra.query.scaladsl.CassandraReadJournal
import akka.persistence.query.PersistenceQuery
import com.datastax.driver.core.Session

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by henry on 3/23/17.
  */
trait CassandraProbe extends ProbeService[Session] {

  implicit val system: ActorSystem

  def target: Future[Session] = {
    val session = PersistenceQuery(system).readJournalFor[CassandraReadJournal](CassandraReadJournal.Identifier).session
    session.underlying().map { s => session.close(); s }
  }

}
