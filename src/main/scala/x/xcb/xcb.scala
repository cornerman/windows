package windows.x.xcb

import scala.scalanative.native._, Unsigned._

@link("xcb")
@extern object XCB {
  type xcb_window_t = CUInt
  type xcb_pixmap_t = CUInt
  type xcb_cursor_t = CUInt
  type xcb_font_t = CUInt
  type xcb_gcontext_t = CUInt
  type xcb_colormap_t = CUInt
  type xcb_atom_t = CUInt
  type xcb_drawable_t = CUInt
  type xcb_fontable_t = CUInt
  type xcb_visualid_t = CUInt
  type xcb_timestamp_t = CUInt
  type xcb_keysym_t = CUInt
  type xcb_keycode_t = CUChar
  type xcb_button_t = CUChar

  @struct class xcb_setup_t(
    val has_error: CInt,
    val fd: CInt,
    val status: CUChar,
    val pad0: CUChar,
    val protocol_major_version: CUChar,
    val protocol_minor_version: CUChar,
    val length: CUChar,
    val release_number: CUInt,
    val resource_id_base: CUInt,
    val resource_id_mask: CUInt,
    val motion_buffer_size: CUInt,
    val vendor_len: CUChar,
    val maximum_request_length: CUChar,
    val roots_len: CUChar,
    val pixmap_formats_len: CUChar,
    val image_byte_order: CUChar,
    val bitmap_format_bit_order: CUChar,
    val bitmap_format_scanline_unit: CUChar,
    val bitmap_format_scanline_pad: CUChar,
    val min_keycode: xcb_keycode_t,
    val max_keycode: xcb_keycode_t,
    val pad1: CUChar)

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
    val white_pixel: CUInt,
    val black_pixel: CUInt,
    val current_input_masks: CUInt,
    val width_in_pixels: CUShort,
    val height_in_pixels: CUShort,
    val width_in_millimeters: CUShort,
    val height_in_millimeters: CUShort,
    val min_installed_maps: CUShort,
    val max_installed_maps: CUShort,
    val root_visual: xcb_visualid_t,
    val backing_stores: CUChar,
    val save_unders: CUChar,
    val root_depth: CUChar,
    val allowed_depths_len: CUChar)

  @struct class xcb_generic_event_t(
    val response_type: CUChar,
    val pad0: CUChar,
    val sequence: CUShort,
    val pad: Array[CUInt], // length 7
    val full_sequence: CUInt)

  @struct class xcb_button_press_event_t(
    val response_type: CUChar,
    val detail: xcb_button_t,
    val sequence: CUShort,
    val time: xcb_timestamp_t,
    val root: xcb_window_t,
    val event: xcb_window_t,
    val child: xcb_window_t,
    val root_x: CShort,
    val root_y: CShort,
    val event_x: CShort,
    val event_y: CShort,
    val state: CUShort,
    val same_screen: CChar,
    val pad0: CChar)

  @struct class xcb_button_release_event_t(
    val response_type: CUChar,
    val detail: xcb_button_t,
    val sequence: CUShort,
    val time: xcb_timestamp_t,
    val root: xcb_window_t,
    val event: xcb_window_t,
    val child: xcb_window_t,
    val root_x: CShort,
    val root_y: CShort,
    val event_x: CShort,
    val event_y: CShort,
    val state: CUShort,
    val same_screen: CChar,
    val pad0: CChar)

  @struct class xcb_key_press_event_t(
    val response_type: CUChar,
    val detail: xcb_keycode_t,
    val sequence: CUShort,
    val time: xcb_timestamp_t,
    val root: xcb_window_t,
    val event: xcb_window_t,
    val child: xcb_window_t,
    val root_x: CShort,
    val root_y: CShort,
    val event_x: CShort,
    val event_y: CShort,
    val state: CUShort,
    val same_screen: CChar,
    val pad0: CChar)

  @struct class xcb_key_release_event_t(
    val response_type: CUChar,
    val detail: xcb_keycode_t,
    val sequence: CUShort,
    val time: xcb_timestamp_t,
    val root: xcb_window_t,
    val event: xcb_window_t,
    val child: xcb_window_t,
    val root_x: CShort,
    val root_y: CShort,
    val event_x: CShort,
    val event_y: CShort,
    val state: CUShort,
    val same_screen: CChar,
    val pad0: CChar)

  @struct class xcb_motion_notify_event_t(
    val response_type: CUChar,
    val detail: CUChar,
    val sequence: CUShort,
    val time: xcb_timestamp_t,
    val root: xcb_window_t,
    val event: xcb_window_t,
    val child: xcb_window_t,
    val root_x: CShort,
    val root_y: CShort,
    val event_x: CShort,
    val event_y: CShort,
    val state: CUShort,
    val same_screen: CChar,
    val pad0: CChar)

  @struct class xcb_map_request_event_t(
    val response_type: CUChar,
    val pad0: CUChar,
    val sequence: CUShort,
    val parent: xcb_window_t,
    val window: xcb_window_t)

  @struct class xcb_map_notify_event_t(
    val response_type: CUChar,
    val pad0: CUChar,
    val sequence: CUShort,
    val event: xcb_window_t,
    val window: xcb_window_t,
    val override_redirect: CUChar,
    val pad1: Array[CUChar]) //length 3

  @struct class xcb_unmap_notify_event_t(
    val response_type: CUChar,
    val pad0: CUChar,
    val sequence: CUShort,
    val event: xcb_window_t,
    val window: xcb_window_t,
    val from_configure: CUChar,
    val pad1: Array[CUChar]) // length 3

  @struct class xcb_void_cookie_t(val sequence: CUInt)
  @struct class xcb_get_geometry_cookie_t(val sequence: CUInt)
  @struct class xcb_grab_pointer_cookie_t(val sequence: CUInt)
  @struct class xcb_query_pointer_cookie_t(val sequence: CUInt)

  @struct class xcb_get_geometry_reply_t(
    val response_type: CUChar,
    val depth: CUChar,
    val sequence: CUShort,
    val length: CUInt,
    val root: xcb_window_t,
    val x: CShort,
    val y: CShort,
    val width: CUShort,
    val height: CUShort,
    val border_width: CUShort,
    val pad0: Array[CUChar]) // length 2

  @struct class xcb_query_pointer_reply_t(
    val response_type: CUChar,
    val same_screen: CUChar,
    val sequence: CUShort,
    val length: CUInt,
    val root: xcb_window_t,
    val child: xcb_window_t,
    val root_x: CShort,
    val root_y: CShort,
    val win_x: CShort,
    val win_y: CShort,
    val mask: CUShort,
    val pad0: Array[CUChar]) // length 2

  @struct class xcb_generic_error_t(
    val response_type: CUChar,
    val error_code: CUChar,
    val sequence: CUShort,
    val resource_id: CUInt,
    val minor_code: CUShort,
    val major_code: CUChar,
    val pad0: CUChar,
    val pad: Array[CUInt], // length 5
    val full_sequence: CUInt)


  def xcb_connect(displayname: CString, screenp: Ptr[Int]): Ptr[xcb_connection_t] = extern
  def xcb_connection_has_error(c: Ptr[xcb_connection_t]): CInt = extern
  def xcb_get_setup(c: Ptr[xcb_connection_t]): Ptr[xcb_setup_t] = extern
  def xcb_setup_roots_iterator(s: Ptr[xcb_setup_t]): Ptr[xcb_screen_t] = extern
  def xcb_flush(c: Ptr[xcb_connection_t]): CInt = extern
  def xcb_disconnect(c: Ptr[xcb_connection_t]): Unit = extern

  def xcb_grab_key(c: Ptr[xcb_connection_t], owner_events: CUChar, grab_window: xcb_window_t, modifiers: CUShort, key: xcb_keycode_t, pointer_mode: CUChar, keyboard_mode: CUChar): xcb_void_cookie_t = extern //TODO xcb_request_check with xcb_grab_key_checked
  def xcb_grab_button(c: Ptr[xcb_connection_t], owner_events: CUChar, grab_window: xcb_window_t, event_mask: CUShort, pointer_mode: CUChar, keyboard_mode: CUChar, confine_to: xcb_window_t, cursor: xcb_cursor_t, button: xcb_button_t, modifiers: CUShort): xcb_void_cookie_t = extern //TODO xcb_request_check with xcb_grab_button_checked

  def xcb_wait_for_event(c: Ptr[xcb_connection_t]): Ptr[xcb_generic_event_t] = extern
  def xcb_poll_for_event(c: Ptr[xcb_connection_t]): Ptr[xcb_generic_event_t] = extern

  def xcb_configure_window(conn: Ptr[xcb_connection_t], window: xcb_window_t, value_mask: CUShort, value_list: Ptr[CUInt]): xcb_void_cookie_t = extern //TODO value_list: Array[CUInt] (implementation of Array/List missing?)
  def xcb_change_window_attributes(conn: Ptr[xcb_connection_t], window: xcb_window_t, value_mask: CUInt, value_list: Ptr[CUInt]): xcb_void_cookie_t = extern
  def xcb_map_window(conn: Ptr[xcb_connection_t], window: xcb_window_t): xcb_void_cookie_t = extern
  def xcb_destroy_window(conn: Ptr[xcb_connection_t], window: xcb_window_t): xcb_void_cookie_t = extern

  def xcb_get_geometry(conn: Ptr[xcb_connection_t], drawable: xcb_drawable_t): xcb_get_geometry_cookie_t = extern
  def xcb_get_geometry_reply(conn: Ptr[xcb_connection_t], cookie: xcb_get_geometry_cookie_t, e: Ptr[Ptr[xcb_generic_error_t]]): Ptr[xcb_get_geometry_reply_t] = extern

  def xcb_warp_pointer(conn: Ptr[xcb_connection_t], src_window: xcb_window_t, dst_window: xcb_window_t, src_x: CShort, src_y: CShort, src_width: CUShort, src_height: CUShort, dst_x: CShort, dst_y: CShort): xcb_void_cookie_t = extern
  def xcb_grab_pointer(conn: Ptr[xcb_connection_t], owner_events: CUChar, grab_window: xcb_window_t, event_mask: CUShort, pointer_mode: CUChar, keyboard_mode: CUChar, confine_to: xcb_window_t, cursor: xcb_cursor_t, time: xcb_timestamp_t): xcb_grab_pointer_cookie_t = extern
  def xcb_ungrab_pointer(conn: Ptr[xcb_connection_t], time: xcb_timestamp_t): xcb_void_cookie_t = extern
  def xcb_query_pointer(conn: Ptr[xcb_connection_t], window: xcb_window_t): xcb_query_pointer_cookie_t = extern
  def xcb_query_pointer_reply(conn: Ptr[xcb_connection_t], cookie: xcb_query_pointer_cookie_t, e: Ptr[Ptr[xcb_generic_error_t]]): Ptr[xcb_query_pointer_reply_t] = extern

}

