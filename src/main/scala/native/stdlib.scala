package windows.native

import scala.scalanative.native._

@extern object stdlib {
  def malloc(size: Word): Ptr[_] = extern
  def exit(status: CInt): Unit = extern
  def system(command: CString): CInt = extern
}

