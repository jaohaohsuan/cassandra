
import org.scalatest.FlatSpec
import org.scalatest.Matchers._

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by henry on 3/23/17.
  */
class ProbeServiceTest extends FlatSpec with ProbeService[Int] {

  def target: Future[Int] = Future { 2 / num }

  implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global

  var num = 1

  import ProbeService._

  behavior of "ProbeServiceTest"

  it should "probe" in {
    probe(target, Retry(1)).onSuccess { case state =>
        assert(state == Reset)
    }
  }

  "Probing" should "be failed" in {
    num = 0
    probe(target, Retry(1)).onSuccess { case state =>
      assert(state == Retry(0))
    }
  }

}
