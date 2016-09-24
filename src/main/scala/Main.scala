package windows

import scala.scalanative.native._, stdio._

// object XMain {
//   import x._
//   def main(args: Array[String]) {
//     setvbuf(stdout, null, _IOLBF, 0) // line buffered
//     X.connect match {
//       case Some(conn) => WM.run(XAdapter.run(conn), XAdapter.act(conn)).foreach(println)
//       case None => println("Error connecting to x")
//     }
//   }
// }

object WaylandMain {
  import wayland._
  def main(args: Array[String]) {
    setvbuf(stdout, null, _IOLBF, 0) // line buffered
    WM.run(WaylandAdapter.run, WaylandAdapter.act).foreach(println)
  }
}
