package windows.xcb

import scala.scalanative.native._
import windows.native._

@link("xcb")
@extern object XCB {
  type xcb_window_t = CUnsignedInt
  type xcb_pixmap_t = CUnsignedInt
  type xcb_cursor_t = CUnsignedInt
  type xcb_font_t = CUnsignedInt
  type xcb_gcontext_t = CUnsignedInt
  type xcb_colormap_t = CUnsignedInt
  type xcb_atom_t = CUnsignedInt
  type xcb_drawable_t = CUnsignedInt
  type xcb_fontable_t = CUnsignedInt
  type xcb_visualid_t = CUnsignedInt
  type xcb_timestamp_t = CUnsignedInt
  type xcb_keysym_t = CUnsignedInt
  type xcb_keycode_t = CUnsignedChar
  type xcb_button_t = CUnsignedChar

  @struct class xcb_setup_t(
    val has_error: CInt,
    val fd: CInt,
    val status: CUnsignedChar,
    val pad0: CUnsignedChar,
    val protocol_major_version: CUnsignedChar,
    val protocol_minor_version: CUnsignedChar,
    val length: CUnsignedChar,
    val release_number: CUnsignedInt,
    val resource_id_base: CUnsignedInt,
    val resource_id_mask: CUnsignedInt,
    val motion_buffer_size: CUnsignedInt,
    val vendor_len: CUnsignedChar,
    val maximum_request_length: CUnsignedChar,
    val roots_len: CUnsignedChar,
    val pixmap_formats_len: CUnsignedChar,
    val image_byte_order: CUnsignedChar,
    val bitmap_format_bit_order: CUnsignedChar,
    val bitmap_format_scanline_unit: CUnsignedChar,
    val bitmap_format_scanline_pad: CUnsignedChar,
    val min_keycode: xcb_keycode_t,
    val max_keycode: xcb_keycode_t,
    val pad1: CUnsignedChar)

  @struct class xcb_connection_t(
    val has_error: CInt,
    val setup: Ptr[xcb_setup_t],
    val fd: CInt,
    val iolock: Any, // TODO: pthread_mutex_t
    val in: Any, // TODO: _xcb_in
    val out: Any, // TODO: _xcb_out
    val ext: Any, // TODO: _xcb_ext
    val xid: Any) // TODO: _xcb_xid

  @struct class xcb_screen_t(
    val root: xcb_window_t,
    val default_colormap: xcb_colormap_t,
    val white_pixel: CUnsignedInt,
    val black_pixel: CUnsignedInt,
    val current_input_masks: CUnsignedInt,
    val width_in_pixels: CUnsignedShort,
    val height_in_pixels: CUnsignedShort,
    val width_in_millimeters: CUnsignedShort,
    val height_in_millimeters: CUnsignedShort,
    val min_installed_maps: CUnsignedShort,
    val max_installed_maps: CUnsignedShort,
    val root_visual: xcb_visualid_t,
    val backing_stores: CUnsignedChar,
    val save_unders: CUnsignedChar,
    val root_depth: CUnsignedChar,
    val allowed_depths_len: CUnsignedChar)

  @struct class xcb_generic_event_t(
    val response_type: CUnsignedChar,
    val pad0: CUnsignedChar,
    val sequence: CUnsignedShort,
    val pad: Array[CUnsignedInt], // length 7
    val full_sequence: CUnsignedInt)

  @struct class xcb_button_press_event_t(
    val response_type: CUnsignedChar,
    val detail: xcb_button_t,
    val sequence: CUnsignedShort,
    val time: xcb_timestamp_t,
    val root: xcb_window_t,
    val event: xcb_window_t,
    val child: xcb_window_t,
    val root_x: CShort,
    val root_y: CShort,
    val event_x: CShort,
    val event_y: CShort,
    val state: CUnsignedShort,
    val same_screen: CChar,
    val pad0: CChar)

  @struct class xcb_button_release_event_t(
    val response_type: CUnsignedChar,
    val detail: xcb_button_t,
    val sequence: CUnsignedShort,
    val time: xcb_timestamp_t,
    val root: xcb_window_t,
    val event: xcb_window_t,
    val child: xcb_window_t,
    val root_x: CShort,
    val root_y: CShort,
    val event_x: CShort,
    val event_y: CShort,
    val state: CUnsignedShort,
    val same_screen: CChar,
    val pad0: CChar)

  @struct class xcb_key_press_event_t(
    val response_type: CUnsignedChar,
    val detail: xcb_keycode_t,
    val sequence: CUnsignedShort,
    val time: xcb_timestamp_t,
    val root: xcb_window_t,
    val event: xcb_window_t,
    val child: xcb_window_t,
    val root_x: CShort,
    val root_y: CShort,
    val event_x: CShort,
    val event_y: CShort,
    val state: CUnsignedShort,
    val same_screen: CChar,
    val pad0: CChar)

  @struct class xcb_key_release_event_t(
    val response_type: CUnsignedChar,
    val detail: xcb_keycode_t,
    val sequence: CUnsignedShort,
    val time: xcb_timestamp_t,
    val root: xcb_window_t,
    val event: xcb_window_t,
    val child: xcb_window_t,
    val root_x: CShort,
    val root_y: CShort,
    val event_x: CShort,
    val event_y: CShort,
    val state: CUnsignedShort,
    val same_screen: CChar,
    val pad0: CChar)

  @struct class xcb_motion_notify_event_t(
    val response_type: CUnsignedChar,
    val detail: CUnsignedChar,
    val sequence: CUnsignedShort,
    val time: xcb_timestamp_t,
    val root: xcb_window_t,
    val event: xcb_window_t,
    val child: xcb_window_t,
    val root_x: CShort,
    val root_y: CShort,
    val event_x: CShort,
    val event_y: CShort,
    val state: CUnsignedShort,
    val same_screen: CChar,
    val pad0: CChar)

  @struct class xcb_void_cookie_t(val sequence: CUnsignedInt)
  @struct class xcb_get_geometry_cookie_t(val sequence: CUnsignedInt)
  @struct class xcb_grab_pointer_cookie_t(val sequence: CUnsignedInt)
  @struct class xcb_query_pointer_cookie_t(val sequence: CUnsignedInt)

  @struct class xcb_get_geometry_reply_t(
    val response_type: CUnsignedChar,
    val depth: CUnsignedChar,
    val sequence: CUnsignedShort,
    val length: CUnsignedInt,
    val root: xcb_window_t,
    val x: CShort,
    val y: CShort,
    val width: CUnsignedShort,
    val height: CUnsignedShort,
    val border_width: CUnsignedShort,
    val pad0: Array[CUnsignedChar]) // length 2

  @struct class xcb_query_pointer_reply_t(
    val response_type: CUnsignedChar,
    val same_screen: CUnsignedChar,
    val sequence: CUnsignedShort,
    val length: CUnsignedInt,
    val root: xcb_window_t,
    val child: xcb_window_t,
    val root_x: CShort,
    val root_y: CShort,
    val win_x: CShort,
    val win_y: CShort,
    val mask: CUnsignedShort,
    val pad0: Array[CUnsignedChar]) // length 2

  @struct class xcb_generic_error_t(
    val response_type: CUnsignedChar,
    val error_code: CUnsignedChar,
    val sequence: CUnsignedShort,
    val resource_id: CUnsignedInt,
    val minor_code: CUnsignedShort,
    val major_code: CUnsignedChar,
    val pad0: CUnsignedChar,
    val pad: Array[CUnsignedInt], // length 5
    val full_sequence: CUnsignedInt)


  def xcb_connect(displayname: CString, screenp: Ptr[Int]): Ptr[xcb_connection_t] = extern
  def xcb_connection_has_error(c: Ptr[xcb_connection_t]): CInt = extern
  def xcb_get_setup(c: Ptr[xcb_connection_t]): Ptr[xcb_setup_t] = extern
  def xcb_setup_roots_iterator(s: Ptr[xcb_setup_t]): Ptr[xcb_screen_t] = extern
  def xcb_flush(c: Ptr[xcb_connection_t]): CInt = extern
  def xcb_disconnect(c: Ptr[xcb_connection_t]): Unit = extern

  def xcb_grab_key(c: Ptr[xcb_connection_t], owner_events: CUnsignedChar, grab_window: xcb_window_t, modifiers: CUnsignedShort, key: xcb_keycode_t, pointer_mode: CUnsignedChar, keyboard_mode: CUnsignedChar): xcb_void_cookie_t = extern //TODO xcb_request_check with xcb_grab_key_checked
  def xcb_grab_button(c: Ptr[xcb_connection_t], owner_events: CUnsignedChar, grab_window: xcb_window_t, event_mask: CUnsignedShort, pointer_mode: CUnsignedChar, keyboard_mode: CUnsignedChar, confine_to: xcb_window_t, cursor: xcb_cursor_t, button: xcb_button_t, modifiers: CUnsignedShort): xcb_void_cookie_t = extern //TODO xcb_request_check with xcb_grab_button_checked

  def xcb_wait_for_event(c: Ptr[xcb_connection_t]): Ptr[xcb_generic_event_t] = extern
  def xcb_poll_for_event(c: Ptr[xcb_connection_t]): Ptr[xcb_generic_event_t] = extern

  def xcb_configure_window(conn: Ptr[xcb_connection_t], window: xcb_window_t, value_mask: CUnsignedShort, value_list: Ptr[CUnsignedInt]): xcb_void_cookie_t = extern //TODO value_list: Array[CUnsignedInt] (implementation of Array/List missing?)
  def xcb_destroy_window(conn: Ptr[xcb_connection_t], window: xcb_window_t): xcb_void_cookie_t = extern

  def xcb_get_geometry(conn: Ptr[xcb_connection_t], drawable: xcb_drawable_t): xcb_get_geometry_cookie_t = extern
  def xcb_get_geometry_reply(conn: Ptr[xcb_connection_t], cookie: xcb_get_geometry_cookie_t, e: Ptr[Ptr[xcb_generic_error_t]]): Ptr[xcb_get_geometry_reply_t] = extern

  def xcb_warp_pointer(conn: Ptr[xcb_connection_t], src_window: xcb_window_t, dst_window: xcb_window_t, src_x: CShort, src_y: CShort, src_width: CUnsignedShort, src_height: CUnsignedShort, dst_x: CShort, dst_y: CShort): xcb_void_cookie_t = extern
  def xcb_grab_pointer(conn: Ptr[xcb_connection_t], owner_events: CUnsignedChar, grab_window: xcb_window_t, event_mask: CUnsignedShort, pointer_mode: CUnsignedChar, keyboard_mode: CUnsignedChar, confine_to: xcb_window_t, cursor: xcb_cursor_t, time: xcb_timestamp_t): xcb_grab_pointer_cookie_t = extern
  def xcb_ungrab_pointer(conn: Ptr[xcb_connection_t], time: xcb_timestamp_t): xcb_void_cookie_t = extern
  def xcb_query_pointer(conn: Ptr[xcb_connection_t], window: xcb_window_t): xcb_query_pointer_cookie_t = extern
  def xcb_query_pointer_reply(conn: Ptr[xcb_connection_t], cookie: xcb_query_pointer_cookie_t, e: Ptr[Ptr[xcb_generic_error_t]]): Ptr[xcb_query_pointer_reply_t] = extern
}

