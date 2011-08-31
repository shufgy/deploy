package com.gu.deploy.tasks

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FlatSpec
import com.gu.deploy.{Output, Log}
import collection.mutable.ListBuffer

class CommandLineTest extends FlatSpec with ShouldMatchers {

  "CommandLine" should "return sensible description for simple commands" in {
    CommandLine(List("ls", "-l")).quoted should be ("ls -l")
  }

  it should "return quoted description for commands with string params with spaces" in {
    CommandLine(List("echo", "this needs to be quoted")).quoted should
      be ("echo \"this needs to be quoted\"")
  }

  class RecordingOutput extends Output {
    val recorded = new ListBuffer[String]()

    def verbose(s: => String) { recorded += "VERBOSE: " + s }
    def info(s: => String) { recorded += "INFO: " + s }
    def warn(s: => String) { recorded += "WARN: " + s }
    def error(s: => String) { recorded += "ERROR: " + s }
    def context[T](s: => String)(block: => T) = {
      recorded += "START-CONTEXT: " + s
      try block finally recorded += "END-CONTEXT: " + s
    }
  }

  it should "execute command and pipe progress results to Logger" in {
    val blackBox = new RecordingOutput

    Log.current.withValue(blackBox) {
      val c = CommandLine(List("echo", "hello"))
      c.run()

      blackBox.recorded.toList should be (
        "START-CONTEXT: $ echo hello" ::
        "INFO: hello" ::
        "END-CONTEXT: $ echo hello" ::
        Nil
      )
    }
  }

  it should "throw wgheb "
}