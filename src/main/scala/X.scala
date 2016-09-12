package windows

import scala.scalanative.native._, stdlib._, stdio._
import xcb._, XCB._

class X(conn: Ptr[xcb_connection_t]) {
  private val screen = xcb_setup_roots_iterator(xcb_get_setup(conn))
  private val root = (!screen).root

  private def createValues(len: Int) = malloc(len * sizeof[CUnsignedInt]).cast[Ptr[CUnsignedInt]]

  def disconnect(): Unit = xcb_disconnect(conn)
  def flush(): Unit = xcb_flush(conn)

  def grabKey(keycode: xcb_keycode_t, modMask: UShort) {
    xcb_grab_key(conn, 0.toUByte, root, modMask, keycode, XCB_GRAB_MODE_ASYNC, XCB_GRAB_MODE_ASYNC)
  }

  def grabButton(button: xcb_button_t, modMask: UShort) {
    xcb_grab_button(conn, 0.toUByte, root, (XCB_EVENT_MASK_BUTTON_PRESS | XCB_EVENT_MASK_BUTTON_RELEASE).toUShort, XCB_GRAB_MODE_ASYNC, XCB_GRAB_MODE_ASYNC, root, XCB_NONE, button, modMask)
  }

  def warpPointerForResize(win: xcb_window_t) {
    val geom = xcb_get_geometry_reply(conn, xcb_get_geometry(conn, win), null)
    xcb_warp_pointer(conn, XCB_NONE, win, 0, 0, 0.toUShort, 0.toUShort, (!geom).width.toShort, (!geom).height.toShort)
  }

  def warpPointerForMove(win: xcb_window_t) {
    xcb_warp_pointer(conn, XCB_NONE, win, 0, 0, 0.toUShort, 0.toUShort, 1, 1)
  }

  def grabPointer(win: xcb_window_t) {
    val values = createValues(1)
    values(0) = XCB_STACK_MODE_ABOVE
    xcb_configure_window(conn, win, XCB_CONFIG_WINDOW_STACK_MODE, values)
    xcb_grab_pointer(conn, 0.toUByte, root, (XCB_EVENT_MASK_BUTTON_RELEASE | XCB_EVENT_MASK_BUTTON_MOTION | XCB_EVENT_MASK_POINTER_MOTION_HINT).toUShort, XCB_GRAB_MODE_ASYNC, XCB_GRAB_MODE_ASYNC, root, XCB_NONE, XCB_CURRENT_TIME)
  }

  def moveToPointer(win: xcb_window_t) {
    val pointerRequest = xcb_query_pointer(conn, root)
    val geomRequest = xcb_get_geometry(conn, win)
    val pointer = xcb_query_pointer_reply(conn, pointerRequest, null)
    val geom = xcb_get_geometry_reply(conn, geomRequest, null)
    val values = createValues(2)
    values(0) = if ((!pointer).root_x.toUShort + (!geom).width > (!screen).width_in_pixels) ((!screen).width_in_pixels - (!geom).width) else (!pointer).root_x.toUShort
    values(1) = if ((!pointer).root_y.toUShort + (!geom).height > (!screen).height_in_pixels) ((!screen).height_in_pixels - (!geom).height) else (!pointer).root_y.toUShort
    xcb_configure_window(conn, win, (XCB_CONFIG_WINDOW_X | XCB_CONFIG_WINDOW_Y).toUShort, values)
  }

  def resizeToPointer(win: xcb_window_t) {
    val pointerRequest = xcb_query_pointer(conn, root)
    val geomRequest = xcb_get_geometry(conn, win)
    val pointer = xcb_query_pointer_reply(conn, pointerRequest, null)
    val geom = xcb_get_geometry_reply(conn, geomRequest, null)
    val values = createValues(2)
    val xDiff = (!pointer).root_x - (!geom).x
    val yDiff = (!pointer).root_y - (!geom).y
    values(0) = if (xDiff > 0) xDiff.toUShort else (!geom).width
    values(1) = if (yDiff > 0) yDiff.toUShort else (!geom).height
    xcb_configure_window(conn, win, (XCB_CONFIG_WINDOW_WIDTH | XCB_CONFIG_WINDOW_HEIGHT).toUShort, values)
  }

  def ungrabPointer() {
    xcb_ungrab_pointer(conn, XCB_CURRENT_TIME)
  }

  def destroyWindow(win: xcb_window_t) {
    xcb_destroy_window(conn, win)
  }

  def waitForEvent(): Ptr[xcb_generic_event_t] = {
    xcb_wait_for_event(conn)
  }
}

object X {
  def connect: Option[X] = {
    val conn = xcb_connect(null, null)
    if (xcb_connection_has_error(conn) == 0) Some(new X(conn)) else None
  }
}
