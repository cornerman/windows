package windows

import windows.msg.Config
import windows.wayland.WaylandAdapter
import windows.x.XAdapter

object WaylandConfig extends Config {
  override val resizeButton = 2 //right
}

object XConfig extends Config {
  override val resizeButton = 3 //right
}

object XMain {
  def main(args: Array[String]) = WM.run(XConfig, XAdapter.run)
}

object WaylandMain {
  def main(args: Array[String]) = WM.run(WaylandConfig, WaylandAdapter.run)
}
