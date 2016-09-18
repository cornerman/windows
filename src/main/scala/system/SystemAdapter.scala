package windows.system

import windows.msg._

object SystemAdapter {
  def act(action: SystemAction): Option[String] = action match {
    case Exit(reason) =>
      println("exiting: " + reason)
      System.exit(0)
      None
    case Command(cmd) =>
      println("executing: " + cmd)
      Commands.execute(cmd)
      None
  }
}
