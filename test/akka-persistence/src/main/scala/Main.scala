import akka.actor.ActorSystem
import akka.persistence.cassandra.query.scaladsl.CassandraReadJournal
import akka.persistence.query.PersistenceQuery

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}

object Main {
  def main(args: Array[String]): Unit = {

    implicit val system = ActorSystem("system")

    implicit val exec = system.dispatcher

    println("Connect to cassandra Journal")

    val readJournal: CassandraReadJournal = PersistenceQuery(system).readJournalFor[CassandraReadJournal](CassandraReadJournal.Identifier)

    readJournal.session.underlying().onComplete {
      case Failure(_) =>
        system.scheduler.scheduleOnce(5 seconds) {
          Await.result(system.terminate(), 3 seconds)
          System.exit(1)
        }
      case _ =>
        system.scheduler.scheduleOnce(5 seconds) {
          Await.result(system.terminate(), 3 seconds)
        }
    }

  }
}
