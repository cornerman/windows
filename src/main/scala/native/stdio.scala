package windows.native

import scala.scalanative.native._

@extern object stdio {
  var stderr: Ptr[_] = extern
  var stdout: Ptr[_] = extern
  def fopen(filename: CString, mode: CString): Ptr[_] = extern
  def fclose(stream: Ptr[_]): CInt = extern
  def printf(format: CString, args: Vararg*): CInt = extern //TODO: Vararg (implementation missing?)
  def fprintf(stream: Ptr[_], format: CString, args: Vararg*): CInt = extern //TODO: Vararg (implementation missing?)
  def fflush(stream: Ptr[_]): CInt = extern
}
