package windows

import scala.scalanative.native._
import native.unistd._

object Commands {
  def execute(cmd: String) {
    if (fork() == 0) {
      setsid()
      execl(c"/bin/sh", c"sh", c"-c", toCString(cmd), 0)
    }
  }
}
