package windows

import scala.scalanative.native._
import native.stdlib._
import native.stdio._
import xcb._, XCB._

//TODO: enum
// object Mode extends Enumeration {
//   type Mode = Value
//   val Normal, Move, Resize = Value
// }
object Mode {
  val Normal = 0
  val Move = 1
  val Resize = 2
}

object State {
  var window = 0
  var mode = Mode.Normal
}

object Main {
  def handleEvent(conn: X, event: Event) {
    //TODO: broken match on extractors: https://github.com/scala-native/scala-native/issues/112
    // event match {
    //   case KeyPressEvent(e) =>
    //   case KeyReleaseEvent(e) =>
    //   case ButtonPressEvent(e) =>
    //   case ButtonReleaseEvent(e) =>
    //   case MotionNotifyEvent(e) =>
    //   case _ =>
    // }
    KeyPressEvent.unapply(event) match {
      case Some(e) =>
        if ((!e).detail == 24) {
          val win = (!e).child
          if (win != 0) conn.destroy(win)
        } else if ((!e).detail == 36) {
          system(c"urxvt &")
        } else if ((!e).detail == 67) {
          conn.disconnect()
          exit(1)
        }
      case None =>
    }
    ButtonPressEvent.unapply(event) match {
      case Some(e) =>
        State.window = (!e).child
        State.mode = if ((!e).detail == 1) Mode.Move else if ((!e).detail == 3) Mode.Resize else Mode.Normal
        if (State.window != 0 && State.mode != Mode.Normal) {
          if (State.mode == Mode.Move) conn.warpPointerForMove(State.window)
          else conn.warpPointerForResize(State.window)
          conn.grabPointer(State.window)
        }
      case None =>
    }
    ButtonReleaseEvent.unapply(event) match {
      case Some(e) =>
        conn.ungrabPointer()
        State.window = 0
        State.mode = Mode.Normal
      case None =>
    }
    MotionNotifyEvent.unapply(event) match {
      case Some(e) =>
        if (State.window != 0) {
          if (State.mode == Mode.Move) conn.moveToPointer(State.window)
          else if (State.mode == Mode.Resize) conn.resizeToPointer(State.window)
        }
      case None =>
    }
  }

  def run(conn: X) {
    conn.registerKeys()
    while (true) {
      val ev = conn.waitForEvent()
      handleEvent(conn, ev)
      fflush(stdout)
    }
  }

  def main(args: Array[String]): Unit = X.connect match {
    case Some(conn) => run(conn)
    case None => fprintf(stderr, c"Error connecting to x")
  }
}
