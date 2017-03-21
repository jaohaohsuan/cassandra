import akka.actor.ActorSystem
import akka.persistence.cassandra.query.scaladsl.CassandraReadJournal
import akka.persistence.query.PersistenceQuery

import scala.io.StdIn

object Main {
  def main(args: Array[String]): Unit = {

    implicit val system = ActorSystem("system")

    println("Connect to cassandra Journal")

    val readJournal = PersistenceQuery(system).readJournalFor[CassandraReadJournal](CassandraReadJournal.Identifier)

    StdIn.readLine()
  }
}
