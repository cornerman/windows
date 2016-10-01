package windows

import scala.scalanative.native._, stdio._
import windows.msg.Config

object MyConfig extends Config

object XMain {
  import x._
  def main(args: Array[String]) {
    setvbuf(stdout, null, _IOLBF, 0) // line buffered
    X.connect match {
      case Some(conn) =>
        val adapter = new XAdapter(conn)
        WM.run(MyConfig, adapter.run, adapter.act).foreach(println)
      case None => println("Error connecting to x")
    }
  }
}

object WaylandMain {
  import wayland._
  def main(args: Array[String]) {
    setvbuf(stdout, null, _IOLBF, 0) // line buffered
    WM.run(MyConfig, WaylandAdapter.run, WaylandAdapter.act).foreach(println)
  }
}
