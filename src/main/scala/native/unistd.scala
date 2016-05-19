package windows.native

import scala.scalanative.native._

@extern object unistd {
  def execl(path: CString, arg: Vararg*): CInt = extern
  def fork(): CInt = extern
  def setsid(): CInt = extern
}
