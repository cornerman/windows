package windows

import scala.scalanative.native._, Unsigned._
import native.stdlib._
import native.stdio._
import xcb._, XCB._

class X(conn: Ptr[xcb_connection_t]) {
  private val screen = xcb_setup_roots_iterator(xcb_get_setup(conn))
  private val root = (!screen).root

  private def createValues(len: Int) = malloc(len * sizeof[CUInt]).cast[Ptr[CUInt]]

  def disconnect(): Unit = xcb_disconnect(conn)
  def flush(): Unit = xcb_flush(conn)

  def registerKeys() {
    xcb_grab_key(conn, 0, root, XCB_MOD_MASK_ANY, 67, XCB_GRAB_MODE_ASYNC, XCB_GRAB_MODE_ASYNC)
    xcb_grab_key(conn, 0, root, XCB_MOD_MASK_1, 24, XCB_GRAB_MODE_ASYNC, XCB_GRAB_MODE_ASYNC)
    xcb_grab_key(conn, 0, root, XCB_MOD_MASK_1, 36, XCB_GRAB_MODE_ASYNC, XCB_GRAB_MODE_ASYNC)

    xcb_grab_button(conn, 0, root, (XCB_EVENT_MASK_BUTTON_PRESS | XCB_EVENT_MASK_BUTTON_RELEASE).toShort, XCB_GRAB_MODE_ASYNC, XCB_GRAB_MODE_ASYNC, root, XCB_NONE, 1, XCB_MOD_MASK_1)

    xcb_grab_button(conn, 0, root, (XCB_EVENT_MASK_BUTTON_PRESS | XCB_EVENT_MASK_BUTTON_RELEASE).toShort, XCB_GRAB_MODE_ASYNC, XCB_GRAB_MODE_ASYNC, root, XCB_NONE, 3, XCB_MOD_MASK_1)

    flush()
  }

  def warpPointerForResize(win: xcb_window_t) {
    val geom = xcb_get_geometry_reply(conn, xcb_get_geometry(conn, win), null)
    xcb_warp_pointer(conn, XCB_NONE, win, 0, 0, 0, 0, (!geom).width, (!geom).height)
  }

  def warpPointerForMove(win: xcb_window_t) {
    xcb_warp_pointer(conn, XCB_NONE, win, 0, 0, 0, 0, 1, 1)
  }

  def grabPointer(win: xcb_window_t) {
    val values = createValues(1)
    values(0) = XCB_STACK_MODE_ABOVE
    xcb_configure_window(conn, win, XCB_CONFIG_WINDOW_STACK_MODE, values)
    xcb_grab_pointer(conn, 0, root, (XCB_EVENT_MASK_BUTTON_RELEASE | XCB_EVENT_MASK_BUTTON_MOTION | XCB_EVENT_MASK_POINTER_MOTION_HINT).toShort, XCB_GRAB_MODE_ASYNC, XCB_GRAB_MODE_ASYNC, root, XCB_NONE, XCB_CURRENT_TIME)

    flush()
  }

  def moveToPointer(win: xcb_window_t) {
    val pointer = xcb_query_pointer_reply(conn, xcb_query_pointer(conn, root), null)
    val geom = xcb_get_geometry_reply(conn, xcb_get_geometry(conn, win), null)
    val values = createValues(2)
    //TODO: why do I need toInt all the time?
    values(0) = if ((!pointer).root_x.toInt + (!geom).width.toInt > (!screen).width_in_pixels.toInt) ((!screen).width_in_pixels.toInt - (!geom).width.toInt) else (!pointer).root_x
    values(1) = if ((!pointer).root_y.toInt + (!geom).height.toInt > (!screen).height_in_pixels.toInt) ((!screen).height_in_pixels.toInt - (!geom).height.toInt) else (!pointer).root_y
    xcb_configure_window(conn, win, (XCB_CONFIG_WINDOW_X.toInt | XCB_CONFIG_WINDOW_Y.toInt).toShort, values)
    flush()
  }

  def resizeToPointer(win: xcb_window_t) {
    val pointer = xcb_query_pointer_reply(conn, xcb_query_pointer(conn, root), null)
    val geom = xcb_get_geometry_reply(conn, xcb_get_geometry(conn, win), null)
    val values = createValues(2)
    //TODO: why do I need toInt all the time?
    val xDiff = (!pointer).root_x.toInt - (!geom).x.toInt
    val yDiff = (!pointer).root_y.toInt - (!geom).y.toInt
    values(0) = if (xDiff > 0) xDiff else (!geom).width.toInt
    values(1) = if (yDiff > 0) yDiff else (!geom).height.toInt
    xcb_configure_window(conn, win, (XCB_CONFIG_WINDOW_WIDTH.toInt | XCB_CONFIG_WINDOW_HEIGHT.toInt).toShort, values)
    flush()
  }

  def ungrabPointer() {
    xcb_ungrab_pointer(conn, XCB_CURRENT_TIME)
    flush()
  }

  def destroy(win: xcb_window_t) {
    xcb_destroy_window(conn, win)
    flush()
  }

  def waitForEvent(): Event = {
    val ev = xcb_wait_for_event(conn)
    val response = (!ev).response_type & ~0x80
    Event(response, ev)
  }
}

object X {
  def connect: Option[X] = {
    val conn = xcb_connect(null, null)
    if (xcb_connection_has_error(conn) == 0) Some(new X(conn)) else None
  }
}
