package windows.wayland

import windows.msg._

import scala.scalanative.native._
import wlc._, WLC._

object WLCHelper {
  def activeMods(modByte: wlc_modifier_bit): Set[Modifier] = {
    Array(Shift, Ctrl, Mod1, Mod2, Mod3, Mod4, Mod5).flatMap { mod =>
      ((translateMod(mod) & modByte) match {
        case 0 => None
        case _ => Some(mod)
      })
    }.toSet
  }

  def translateMod(mod: Modifier): wlc_modifier_bit = mod match {
    case Shift => WLC_BIT_MOD_SHIFT
    case Lock  => ???
    case Ctrl  => WLC_BIT_MOD_CTRL
    case Mod1  => WLC_BIT_MOD_ALT
    case Mod2  => WLC_BIT_MOD_MOD2
    case Mod3  => WLC_BIT_MOD_MOD3
    case Mod4  => WLC_BIT_MOD_LOGO
    case Mod5  => WLC_BIT_MOD_MOD5
  }
}

object WaylandAdapter {
  import windows.system.Commands
  import WLCHelper._

  def act(action: Action): Unit = action match {
    case Exit(reason) =>
      wlc_terminate()
    case Command(cmd) =>
      // wlc_exec(toCString(cmd), Array(toCString(cmd), null))
      Commands.execute(cmd)
    case Focus(window) =>
      wlc_view_bring_to_front(window)
      wlc_view_focus(window)
    case Close(window) =>
      wlc_view_close(window)
    case WarpPointer(window, point) =>
      val p = stackalloc[wlc_point]
      !p = new wlc_point(point.x, point.y)
      wlc_pointer_set_position(p)
    case MoveWindow(window, point) if window != 0 =>
      wlc_view_bring_to_front(window)
      val geom = wlc_view_get_geometry(window)
      val g = stackalloc[wlc_geometry]
      !g = new wlc_geometry(new wlc_point(point.x, point.y), (!geom).size)
      wlc_view_set_geometry(window, WLC_RESIZE_EDGE_NONE, g)
    case ResizeWindow(window, point: Point) if window != 0 =>
      wlc_view_bring_to_front(window)
      val geom = wlc_view_get_geometry(window)
      val g = stackalloc[wlc_geometry]
      !g = new wlc_geometry((!geom).origin, new wlc_size(point.x, point.y))
      wlc_view_set_geometry(window, WLC_RESIZE_EDGE_BOTTOM, g)
    case ManageWindow(window) =>
      wlc_view_set_mask(window, wlc_output_get_mask(wlc_view_get_output(window)))
      wlc_view_bring_to_front(window)
      wlc_view_focus(window)
    case _ =>
  }

  var handler: Event => Boolean = _
  def run(eventHandler: Event => EventResult): Option[String] = {
    //TODO closure broken
    this.handler = ev => {
      val res = eventHandler(ev)
      res.actions.foreach(act)
      res.handled
    }

    //TODO: why need to inline?
    wlc_log_set_handler(
      (logType: wlc_log_type, msg: CString) => {
        handler(LogEvent(Log.Info(fromCString(msg)))) //TODO logtype
        ()
      }
    )

    wlc_set_output_created_cb(
      (output: wlc_handle) => {
        handler(LogEvent(Log.Info("output created")))
        true
      }
    )

    wlc_set_output_destroyed_cb(
      (output: wlc_handle) => {
        handler(LogEvent(Log.Info("output destroyed")))
        ()
      }
    )

    wlc_set_output_focus_cb(
      (output: wlc_handle, focus: Boolean) => {
        handler(LogEvent(Log.Info("output focus")))
        ()
      }
    )

    wlc_set_view_created_cb(
      (view: wlc_handle) => {
        val handled = handler(MapRequestEvent(view))
        if (handled) handler(MapNotifyEvent(view))
        else false
      }
    )

    wlc_set_view_destroyed_cb(
      (view: wlc_handle) => {
        handler(UnmapNotifyEvent(view))
        ()
      }
    )

    wlc_set_view_focus_cb(
      (view: wlc_handle, focus: Boolean) => {
        val handled = handler(FocusEvent(view, focus))
        wlc_view_set_state(view, WLC_BIT_ACTIVATED, handled)
      }
    )

    wlc_set_view_move_to_output_cb(
      (view: wlc_handle, from_output: wlc_handle, to_output: wlc_handle) => {
        handler(LogEvent(Log.Info("view move to output")))
        ()
      }
    )

    wlc_set_keyboard_key_cb(
      (view: wlc_handle, time: Int, modifiers: Ptr[wlc_modifiers], key: Int, state: wlc_key_state) => {
        val mods = activeMods((!modifiers).mods)
        val ev = state match {
          case `WLC_KEY_STATE_PRESSED`  => KeyPressEvent(view, key + 8, mods) //why?
          case `WLC_KEY_STATE_RELEASED` => KeyReleaseEvent(view, key + 8, mods)
        }
        handler(ev)
      }
    )

    wlc_set_pointer_button_cb(
      (view: wlc_handle, time: Int, modifiers: Ptr[wlc_modifiers], button: Int, state: wlc_button_state) => {
        val mods = activeMods((!modifiers).mods)
        val position = stackalloc[wlc_point]
        wlc_pointer_get_position(position)
        val point = Point((!position).x, (!position).y)
        val ev = state match {
          case `WLC_BUTTON_STATE_PRESSED`  => ButtonPressEvent(view, button - 271, mods, point) // why?
          case `WLC_BUTTON_STATE_RELEASED` => ButtonReleaseEvent(view, button - 271, mods, point)
        }
        handler(ev)
      }
    )

    wlc_set_pointer_motion_cb(
      (view: wlc_handle, time: Int, point: Ptr[wlc_point]) => {
        handler(MotionNotifyEvent(view, Point((!point).x, (!point).y)))
      }
    )

    wlc_init() match {
      case false => Some("error initializing wlc")
      case true  => wlc_run(); None
    }
  }
}
