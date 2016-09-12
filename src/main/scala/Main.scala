package windows

import scala.scalanative.native._, stdio._

object Main {
  def main(args: Array[String]): Unit = X.connect match {
    case Some(conn) =>
      val wm = new WM(conn)
      wm.grabKeys()
      while (true) {
        wm.tick()
      }
    case None => fprintf(stderr, c"Error connecting to x")
  }
}
