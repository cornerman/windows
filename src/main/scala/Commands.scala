package windows

import scala.scalanative.native._
import native.unistd._

object Commands {
  //TODO: normal string. string to cstring?
  def execute(cmd: CString) {
    if (fork() == 0) {
      setsid()
      execl(c"/bin/sh", c"sh", c"-c", cmd, 0)
    }
  }
}
