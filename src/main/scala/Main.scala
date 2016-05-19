package windows

import scala.scalanative.native._
import native.stdio._

object Main {
  def main(args: Array[String]): Unit = X.connect match {
    case Some(conn) =>
      val wm = stackalloc[WM]
      !wm = new WM(conn)
      (!wm).registerKeys()
      while (true) {
        (!wm).tick()
      }
    case None => fprintf(stderr, c"Error connecting to x")
  }
}
