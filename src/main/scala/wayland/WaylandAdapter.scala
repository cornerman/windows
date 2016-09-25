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
    case MouseMoveStart(window) if window != 0 =>
      println("movestart")
      println(window)
      None
    case MouseResizeStart(window) if window != 0 =>
      println("resizestart")
      println(window)
      None
    case MouseMoving(window) if window != 0 =>
      println("moving")
      println(window)
      None
    case MouseResizing(window) if window != 0 =>
      println("resizing")
      println(window)
      None
    case MouseMoveEnd(window) if window != 0 =>
      println("moveend")
      println(window)
      None
    case MouseResizeEnd(window) if window != 0 =>
      println("resizeend")
      println(window)
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

    wlc_set_output_created_cb((ouput: wlc_handle) => {
      println("output created")
      true
    })

    wlc_set_output_destroyed_cb((ouput: wlc_handle) => {
      println("output destroyed")
    })

    wlc_set_output_focus_cb((ouput: wlc_handle, focus: Boolean) => {
      println("output focus")
    })

    wlc_set_view_created_cb((view: wlc_handle) => {
      println("view created")
      this.handler(MapRequestEvent(view))
      this.handler(MapNotifyEvent(view))
      true
    })

    wlc_set_view_destroyed_cb(
      (view: wlc_handle) => {
        println("view destroyed")
        this.handler(UnmapNotifyEvent(view))
        // wlc_view_focus(get_topmost(wlc_view_get_output(view), 0));
      })

    wlc_set_view_focus_cb(
      (view: wlc_handle, focus: Boolean) => {
        println("view focus")
        wlc_view_set_state(view, WLC_BIT_ACTIVATED, focus)
      })

    wlc_set_view_move_to_output_cb(
      (view: wlc_handle, from_output: wlc_handle, to_output: wlc_handle) => {
        println("view move to output")
      })

    wlc_set_keyboard_key_cb(
      (view: wlc_handle, time: Int, modifiers: Ptr[wlc_modifiers], key: Int, state: wlc_key_state) => {
        println("key")
        val mods = activeMods((!modifiers).mods)
        println("mods")
        val ev = state match {
          case `WLC_KEY_STATE_PRESSED` => KeyPressEvent(view, key + 8, mods) //why?
          case `WLC_KEY_STATE_RELEASED` => KeyReleaseEvent(view, key + 8, mods)
        }
        this.handler(ev)
        true
      })

    wlc_set_pointer_button_cb(
      (view: wlc_handle, time: Int, modifiers: Ptr[wlc_modifiers], button: Int, state: wlc_button_state) => {
        println("button")
        val mods = activeMods((!modifiers).mods)
        val ev = state match {
          case `WLC_BUTTON_STATE_PRESSED` => ButtonPressEvent(view, button - 271, mods) // why?
          case `WLC_BUTTON_STATE_RELEASED` => ButtonReleaseEvent(view, button - 271, mods)
        }
        this.handler(ev)
        true
      })

    wlc_set_pointer_motion_cb((view: wlc_handle, time: Int, point: Ptr[wlc_point]) => {
      println("pointer motion")
      this.handler(MotionNotifyEvent(view))
      true
    })

    wlc_set_compositor_ready_cb(() => println("ready"))

    wlc_init() match {
      case false => Some("error initializing wlc")
      case true => wlc_run(); None
    }
  }
}
