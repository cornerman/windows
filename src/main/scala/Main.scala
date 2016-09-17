package windows

import scala.scalanative.native._, stdio._

object XMain {
  import x._
  def main(args: Array[String]): Unit = X.connect match {
    case Some(conn) => WM.run(XAdapter.run(conn), XAdapter.act(conn))
    case None => println("Error connecting to x")
  }
}
