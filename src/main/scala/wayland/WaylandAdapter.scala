package windows.wayland

import windows.msg._

import scala.scalanative.native._
import windows.system.Commands
import wlc._, WLC._

object WLCHelper {
  import windows.msg.Modifier, Modifier._

  def activeMods(modByte: wlc_modifier_bit): Set[Modifier] = {
    Modifier.values.filterNot(_ == Lock).flatMap { mod =>
      (translateMod(mod) & modByte) match {
        case 0 => None
        case _ => Some(mod)
      }
    }.toSet
  }

  def translateMod(mod: Modifier): wlc_modifier_bit = mod match {
    case Shift => WLC_BIT_MOD_SHIFT
    case Lock => ???
    case Ctrl => WLC_BIT_MOD_CTRL
    case Mod1 => WLC_BIT_MOD_ALT
    case Mod2 => WLC_BIT_MOD_MOD2
    case Mod3 => WLC_BIT_MOD_MOD3
    case Mod4 => WLC_BIT_MOD_LOGO
    case Mod5 => WLC_BIT_MOD_MOD5
  }
}

object WaylandAdapter {
  import WLCHelper._

  def act(action: ConnectionAction): Option[String] = action match {
    case Configure(config) =>
      println("configure")
      None
    case Exit(reason) =>
      println("exit")
      println(reason)
      wlc_terminate()
      None
    case Command(cmd) =>
      println("command")
      println(cmd)
      // wlc_exec(toCString(cmd), Array(toCString(cmd), null))
      Commands.execute(cmd)
      None
    case Close(window) =>
      println("close")
      println(window)
      wlc_view_close(window)
      None
    case WarpPointer(window, x, y) =>
      println("wrappointer")
      println(window)
      val point = stackalloc[wlc_point]
      !point = new wlc_point(x, y)
      wlc_pointer_set_position(point)
      None
    case MoveWindow(window, x, y) if window != 0 =>
      println("moving")
      println(window)
      val geom = wlc_view_get_geometry(window)
      val g = stackalloc[wlc_geometry]
      !g = new wlc_geometry(new wlc_point(x, y), (!geom).size)
      wlc_view_set_geometry(window, WLC_RESIZE_EDGE_NONE, g)
      None
    case ResizeWindow(window, x, y) if window != 0 =>
      println("resizing")
      println(window)
      val geom = wlc_view_get_geometry(window)
      val g = stackalloc[wlc_geometry]
      !g = new wlc_geometry((!geom).origin, new wlc_size(x, y))
      wlc_view_set_geometry(window, WLC_RESIZE_EDGE_BOTTOM, g)
      None
    case ManageWindow(window) =>
      println("manage")
      println(window)
      wlc_view_set_mask(window, wlc_output_get_mask(wlc_view_get_output(window)))
      wlc_view_bring_to_front(window)
      wlc_view_focus(window)
      None
    case _ =>
      println("ignored action")
      None
  }

  var handler: Event => Unit = _
  def run(handler: Event => Unit): Option[String] = {
    //TODO closure broken
    this.handler = handler

    //TODO: why need to inline?
    wlc_log_set_handler((logType: wlc_log_type, msg: CString) => {
      println(fromCString(msg))
    })

    wlc_set_output_created_cb((output: wlc_handle) => {
      println("output created")
      true
    })

    wlc_set_output_destroyed_cb((output: wlc_handle) => {
      println("output destroyed")
    })

    wlc_set_output_focus_cb((output: wlc_handle, focus: Boolean) => {
      println("output focus")
    })

    wlc_set_view_created_cb((view: wlc_handle) => {
      println("view created")
      println(view)
      this.handler(MapRequestEvent(view))
      this.handler(MapNotifyEvent(view))
      true
    })

    wlc_set_view_destroyed_cb(
      (view: wlc_handle) => {
        println("view destroyed")
        println(view)
        this.handler(UnmapNotifyEvent(view))
        // wlc_view_focus(get_topmost(wlc_view_get_output(view), 0));
      })

    wlc_set_view_focus_cb(
      (view: wlc_handle, focus: Boolean) => {
        println("view focus")
        println(view)
        wlc_view_set_state(view, WLC_BIT_ACTIVATED, focus)
      })

    wlc_set_view_move_to_output_cb(
      (view: wlc_handle, from_output: wlc_handle, to_output: wlc_handle) => {
        println("view move to output")
      })

    //TODO: pointer, key, motion need to know whether this event was handled, pass on to window?
    wlc_set_keyboard_key_cb(
      (view: wlc_handle, time: Int, modifiers: Ptr[wlc_modifiers], key: Int, state: wlc_key_state) => {
        val mods = activeMods((!modifiers).mods)
        val ev = state match {
          case `WLC_KEY_STATE_PRESSED` => KeyPressEvent(view, key + 8, mods) //why?
          case `WLC_KEY_STATE_RELEASED` => KeyReleaseEvent(view, key + 8, mods)
        }
        this.handler(ev)
        false
      })

    wlc_set_pointer_button_cb(
      (view: wlc_handle, time: Int, modifiers: Ptr[wlc_modifiers], button: Int, state: wlc_button_state) => {
        val mods = activeMods((!modifiers).mods)
        val ev = state match {
          case `WLC_BUTTON_STATE_PRESSED` => ButtonPressEvent(view, button - 271, mods, 0, 0) // why?
          case `WLC_BUTTON_STATE_RELEASED` => ButtonReleaseEvent(view, button - 271, mods, 0, 0)
        }
        this.handler(ev)
        false
      })

    wlc_set_pointer_motion_cb((view: wlc_handle, time: Int, point: Ptr[wlc_point]) => {
      this.handler(MotionNotifyEvent(view, (!point).x, (!point).y))
      false
    })

    wlc_set_compositor_ready_cb(() => println("ready"))

    wlc_init() match {
      case false => Some("error initializing wlc")
      case true => wlc_run(); None
    }
  }
}