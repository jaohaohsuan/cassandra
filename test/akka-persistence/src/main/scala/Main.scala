import akka.actor.{ActorSystem, Props}
import akka.persistence.cassandra.journal.CassandraJournalConfig
import akka.persistence.cassandra.query.scaladsl.CassandraReadJournal
import akka.persistence.query.PersistenceQuery
import com.datastax.driver.core.Cluster
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging

object Main extends App with LazyLogging {

  val config = ConfigFactory.load()

  logger.info(s"${config.getStringList("cassandra-journal.contact-points")}")

  implicit val system = ActorSystem("system")

  implicit val ec = system.dispatcher

  //system.actorOf(Props[CassandraProbeActor])

  //PersistenceQuery(system).readJournalFor[CassandraReadJournal](CassandraReadJournal.Identifier)
  //new CassandraJournalConfig(system, system.settings.config.getConfig("cassandra-journal"))
  //PersistenceQuery(system).readJournalFor[CassandraReadJournal](CassandraReadJournal.Identifier)
}
