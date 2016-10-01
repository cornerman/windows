package windows.x

import scala.scalanative.native._, stdlib._, stdio._
import Unsigned._
import xcb._, XCB._

object XHelper {
  import windows.msg._

  def activeMods(modByte: Short): Set[Modifier] = {
    Array(Shift, Lock, Ctrl, Mod1, Mod2, Mod3, Mod4, Mod5).flatMap { mod =>
      (translateMod(mod) & modByte) match {
        case 0 => None
        case _ => Some(mod)
      }
    }.toSet
  }

  def translateMod(mod: Modifier): xcb_mod_mask_t  = mod match {
    case Shift => XCB_MOD_MASK_SHIFT
    case Lock => XCB_MOD_MASK_LOCK
    case Ctrl => XCB_MOD_MASK_CONTROL
    case Mod1 => XCB_MOD_MASK_1
    case Mod2 => XCB_MOD_MASK_2
    case Mod3 => XCB_MOD_MASK_3
    case Mod4 => XCB_MOD_MASK_4
    case Mod5 => XCB_MOD_MASK_5
  }

  //TODO
  implicit def longToUInt(value: Long): xcb_window_t = value.toInt;
}

class X(conn: Ptr[xcb_connection_t]) {
  private val screen = xcb_setup_roots_iterator(xcb_get_setup(conn))
  private val root = (!screen).root

  private def createValues(len: Int) = malloc(len * sizeof[CUInt]).cast[Ptr[CUInt]]

  def disconnect(): Unit = xcb_disconnect(conn)
  def flush(): Unit = xcb_flush(conn)

  def registerEvents() {
    val mask = XCB_CW_EVENT_MASK
    val values = createValues(1)
    values(0) = XCB_EVENT_MASK_SUBSTRUCTURE_REDIRECT |
                XCB_EVENT_MASK_SUBSTRUCTURE_NOTIFY
                // XCB_EVENT_MASK_ENTER_WINDOW |
                // XCB_EVENT_MASK_LEAVE_WINDOW |
                // XCB_EVENT_MASK_STRUCTURE_NOTIFY |
                // XCB_EVENT_MASK_PROPERTY_CHANGE |
                // XCB_EVENT_MASK_FOCUS_CHANGE

    xcb_change_window_attributes(conn, root, mask, values);
    free(values)
  }

  def grabKey(keycode: xcb_keycode_t, modMask: xcb_mod_mask_t) {
    xcb_grab_key(conn, 0, root, modMask, keycode, XCB_GRAB_MODE_ASYNC, XCB_GRAB_MODE_ASYNC)
  }

  def grabButton(button: xcb_button_t, modMask: xcb_mod_mask_t) {
    xcb_grab_button(conn, 0, root, (XCB_EVENT_MASK_BUTTON_PRESS | XCB_EVENT_MASK_BUTTON_RELEASE).toShort, XCB_GRAB_MODE_ASYNC, XCB_GRAB_MODE_ASYNC, root, XCB_NONE, button, modMask)
  }

  def manageWindow(win: xcb_window_t) {
    xcb_map_window(conn, win)
  }

  def warpPointer(win: xcb_window_t, x: Int, y: Int) {
    xcb_warp_pointer(conn, XCB_NONE, win, 0, 0, 0, 0, x.toShort, y.toShort)
  }

  // def warpPointerForMove(win: xcb_window_t) {
  //   xcb_warp_pointer(conn, XCB_NONE, win, 0, 0, 0, 0, 1, 1)
  // }

  def bringToFront(win: xcb_window_t) {
    val values = createValues(1)
    values(0) = XCB_STACK_MODE_ABOVE
    xcb_configure_window(conn, win, XCB_CONFIG_WINDOW_STACK_MODE, values)
    free(values)
  }

  def grabPointer() {
    xcb_grab_pointer(conn, 0, root, (XCB_EVENT_MASK_BUTTON_RELEASE | XCB_EVENT_MASK_BUTTON_MOTION | XCB_EVENT_MASK_POINTER_MOTION_HINT).toShort, XCB_GRAB_MODE_ASYNC, XCB_GRAB_MODE_ASYNC, root, XCB_NONE, XCB_CURRENT_TIME)
  }

  def moveWindow(win: xcb_window_t, x: Int, y: Int) {
    val values = createValues(2)
    values(0) = x
    values(1) = y
    xcb_configure_window(conn, win, (XCB_CONFIG_WINDOW_X | XCB_CONFIG_WINDOW_Y).toShort, values)
    free(values)
  }

//   def moveToPointer(win: xcb_window_t) {
//     val pointerRequest = xcb_query_pointer(conn, root)
//     val geomRequest = xcb_get_geometry(conn, win)
//     val pointer = xcb_query_pointer_reply(conn, pointerRequest, null)
//     val geom = xcb_get_geometry_reply(conn, geomRequest, null)
//     val values = createValues(2)
//     values(0) = if ((!pointer).root_x + (!geom).width > (!screen).width_in_pixels) ((!screen).width_in_pixels - (!geom).width) else (!pointer).root_x
//     values(1) = if ((!pointer).root_y + (!geom).height > (!screen).height_in_pixels) ((!screen).height_in_pixels - (!geom).height) else (!pointer).root_y
//     xcb_configure_window(conn, win, (XCB_CONFIG_WINDOW_X | XCB_CONFIG_WINDOW_Y).toShort, values)
//     free(values)
//   }

  def resizeWindow(win: xcb_window_t, x: Int, y: Int) {
    val values = createValues(2)
    values(0) = x
    values(1) = y
    xcb_configure_window(conn, win, (XCB_CONFIG_WINDOW_WIDTH | XCB_CONFIG_WINDOW_HEIGHT).toShort, values)
    free(values)
  }

  // def resizeToPointer(win: xcb_window_t) {
  //   val pointerRequest = xcb_query_pointer(conn, root)
  //   val geomRequest = xcb_get_geometry(conn, win)
  //   val pointer = xcb_query_pointer_reply(conn, pointerRequest, null)
  //   val geom = xcb_get_geometry_reply(conn, geomRequest, null)
  //   val values = createValues(2)
  //   val xDiff = (!pointer).root_x - (!geom).x
  //   val yDiff = (!pointer).root_y - (!geom).y
  //   values(0) = if (xDiff > 0) xDiff else (!geom).width
  //   values(1) = if (yDiff > 0) yDiff else (!geom).height
  //   xcb_configure_window(conn, win, (XCB_CONFIG_WINDOW_WIDTH | XCB_CONFIG_WINDOW_HEIGHT).toShort, values)
  //   free(values)
  // }

  def ungrabPointer() {
    xcb_ungrab_pointer(conn, XCB_CURRENT_TIME)
  }

  def destroyWindow(win: xcb_window_t) {
    xcb_destroy_window(conn, win)
  }

  def waitForEvent() = xcb_wait_for_event(conn)
}

object X {
  def connect: Option[X] = {
    val conn = xcb_connect(null, null)
    if (xcb_connection_has_error(conn) == 0) Some(new X(conn)) else None
  }
}
