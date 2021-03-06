package magenta

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class HostListTests extends FlatSpec with ShouldMatchers {
  import HostList._
  
  it should ("provide a unique, alphabetical list of supported apps") in {
    val hostList = List(Host("z", Set(App("z"))), Host("xz", Set(App("x"), App("z"))))
    hostList.supportedApps should be ("x, z")
  }

  it should ("provide an alphabetical list of all hosts and supported apps") in {
    val hostList = List(Host("z.z.z.z", Set(App("z"))), Host("x.x.x.x", Set(App("x"))))
    hostList.dump should be (" x.x.x.x: x\n z.z.z.z: z")
  }
}