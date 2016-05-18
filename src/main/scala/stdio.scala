package windows.native

@extern object stdio {
  var stderr: Ptr[_] = extern
  var stdout: Ptr[_] = extern
  def fopen(filename: CString, mode: CString): Ptr[_] = extern
  def fclose(stream: Ptr[_]): CInt = extern
  def printf(format: CString, args: Any*): CInt = extern
  def fprintf(stream: Ptr[_], format: CString, args: Any*): CInt = extern
  def fflush(stream: Ptr[_]): CInt = extern
}
