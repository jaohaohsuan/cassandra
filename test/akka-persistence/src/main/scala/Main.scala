import akka.actor.ActorSystem
import akka.persistence.cassandra.query.scaladsl.CassandraReadJournal
import akka.persistence.query.PersistenceQuery
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import akka.pattern.after
import akka.actor.Scheduler
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}

object Main {
  def main(args: Array[String]): Unit = {

    implicit val system = ActorSystem("system")

    implicit val exec = system.dispatcher

    implicit val s = system.scheduler

    println("Connect to cassandra Journal")

    val readJournal: CassandraReadJournal = PersistenceQuery(system).readJournalFor[CassandraReadJournal](CassandraReadJournal.Identifier)

    def retry[T](f: => Future[T], retries: Int, defaultDelay: FiniteDuration )(implicit ec: ExecutionContext, s: Scheduler): Future[T] = {
      f recoverWith { case _ if retries > 0 => after(defaultDelay, s)(retry(f, retries - 1 , defaultDelay)) }
    }

    def x = { readJournal.session.underlying() }

    retry(x, 10, 3 seconds).onComplete {
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
