import org.scalatest._

/**
  * Created by henry on 3/23/17.
  */
class RetryTerminationDecisionTest extends org.scalatest.FlatSpec with Matchers with RetryTerminationDecision {

  import ProbeService._
  import RetryTerminationDecision._

  var result = 0

  def doNothing(): Unit = {}

  def passed(): Unit = { result = -1 }
  def failed(): Unit = { result = 10 }
  def continue(): Unit = { result = 1 }

  "Reset" should "pass" in {
    determine(Reset, Decision(passed, doNothing, doNothing))()
    assert(result == -1)
  }

  "Retry(0)" should "fail" in {
    determine(Retry(0), Decision(doNothing, failed, doNothing))()
    assert(result == 10)
  }

  "Retry(>0)" should "continue" in {
    determine(Retry(1), Decision(doNothing, doNothing, continue))()
    assert(result == 1)
  }

}
