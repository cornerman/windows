package windows.xcb

import scala.scalanative.native._, Unsigned._

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

  @struct class xcb_setup_t {
    val has_error: CInt = extern
    val fd: CInt = extern
    val status: CUChar = extern
    val pad0: CUChar = extern
    val protocol_major_version: CUChar = extern
    val protocol_minor_version: CUChar = extern
    val length: CUChar = extern
    val release_number: CUInt = extern
    val resource_id_base: CUInt = extern
    val resource_id_mask: CUInt = extern
    val motion_buffer_size: CUInt = extern
    val vendor_len: CUChar = extern
    val maximum_request_length: CUChar = extern
    val roots_len: CUChar = extern
    val pixmap_formats_len: CUChar = extern
    val image_byte_order: CUChar = extern
    val bitmap_format_bit_order: CUChar = extern
    val bitmap_format_scanline_unit: CUChar = extern
    val bitmap_format_scanline_pad: CUChar = extern
    val min_keycode: xcb_keycode_t = extern
    val max_keycode: xcb_keycode_t = extern
    val pad1: CUChar = extern
  }

  @struct class xcb_connection_t {
    val has_error: CInt = extern
    val setup: Ptr[xcb_setup_t] = extern
    val fd: CInt = extern
    val iolock: Any = extern // TODO: pthread_mutex_t
    val in: Any = extern // TODO: _xcb_in
    val out: Any = extern // TODO: _xcb_out
    val ext: Any = extern // TODO: _xcb_ext
    val xid: Any = extern // TODO: _xcb_xid
  }

  @struct class xcb_screen_t {
    val root: xcb_window_t = extern
    val default_colormap: xcb_colormap_t = extern
    val white_pixel: CUInt = extern
    val black_pixel: CUInt = extern
    val current_input_masks: CUInt = extern
    val width_in_pixels: CUShort = extern
    val height_in_pixels: CUShort = extern
    val width_in_millimeters: CUShort = extern
    val height_in_millimeters: CUShort = extern
    val min_installed_maps: CUShort = extern
    val max_installed_maps: CUShort = extern
    val root_visual: xcb_visualid_t = extern
    val backing_stores: CUChar = extern
    val save_unders: CUChar = extern
    val root_depth: CUChar = extern
    val allowed_depths_len: CUChar = extern
  }

  @struct class xcb_generic_event_t {
    val response_type: CUChar = extern
    val pad0: CUChar = extern
    val sequence: CUShort = extern
    val pad: Array[CUInt] = extern // length 7
    val full_sequence: CUInt = extern
  }

  @struct class xcb_button_press_event_t {
    val response_type: CUChar = extern
    val detail: xcb_button_t = extern
    val sequence: CUShort = extern
    val time: xcb_timestamp_t = extern
    val root: xcb_window_t = extern
    val event: xcb_window_t = extern
    val child: xcb_window_t = extern
    val root_x: CShort = extern
    val root_y: CShort = extern
    val event_x: CShort = extern
    val event_y: CShort = extern
    val state: CUShort = extern
    val same_screen: CChar = extern
    val pad0: CChar = extern
  }

  @struct class xcb_button_release_event_t {
    val response_type: CUChar = extern
    val detail: xcb_button_t = extern
    val sequence: CUShort = extern
    val time: xcb_timestamp_t = extern
    val root: xcb_window_t = extern
    val event: xcb_window_t = extern
    val child: xcb_window_t = extern
    val root_x: CShort = extern
    val root_y: CShort = extern
    val event_x: CShort = extern
    val event_y: CShort = extern
    val state: CUShort = extern
    val same_screen: CChar = extern
    val pad0: CChar = extern
  }

  @struct class xcb_key_press_event_t {
    val response_type: CUChar = extern
    val detail: xcb_keycode_t = extern
    val sequence: CUShort = extern
    val time: xcb_timestamp_t = extern
    val root: xcb_window_t = extern
    val event: xcb_window_t = extern
    val child: xcb_window_t = extern
    val root_x: CShort = extern
    val root_y: CShort = extern
    val event_x: CShort = extern
    val event_y: CShort = extern
    val state: CUShort = extern
    val same_screen: CChar = extern
    val pad0: CChar = extern
  }

  @struct class xcb_key_release_event_t {
    val response_type: CUChar = extern
    val detail: xcb_keycode_t = extern
    val sequence: CUShort = extern
    val time: xcb_timestamp_t = extern
    val root: xcb_window_t = extern
    val event: xcb_window_t = extern
    val child: xcb_window_t = extern
    val root_x: CShort = extern
    val root_y: CShort = extern
    val event_x: CShort = extern
    val event_y: CShort = extern
    val state: CUShort = extern
    val same_screen: CChar = extern
    val pad0: CChar = extern
  }

  @struct class xcb_motion_notify_event_t {
    val response_type: CUChar = extern
    val detail: CUChar = extern
    val sequence: CUShort = extern
    val time: xcb_timestamp_t = extern
    val root: xcb_window_t = extern
    val event: xcb_window_t = extern
    val child: xcb_window_t = extern
    val root_x: CShort = extern
    val root_y: CShort = extern
    val event_x: CShort = extern
    val event_y: CShort = extern
    val state: CUShort = extern
    val same_screen: CChar = extern
    val pad0: CChar = extern
  }

  @struct class xcb_void_cookie_t {
    val sequence: CUInt = extern
  }

  @struct class xcb_get_geometry_cookie_t {
    val sequence: CUInt = extern
  }

  @struct class xcb_grab_pointer_cookie_t {
    val sequence: CUInt = extern
  }

  @struct class xcb_query_pointer_cookie_t {
    val sequence: CUInt = extern
  }

  @struct class xcb_get_geometry_reply_t {
    val response_type: CUChar = extern
    val depth: CUChar = extern
    val sequence: CUShort = extern
    val length: CUInt = extern
    val root: xcb_window_t = extern
    val x: CShort = extern
    val y: CShort = extern
    val width: CUShort = extern
    val height: CUShort = extern
    val border_width: CUShort = extern
    val pad0: Array[CUChar] = extern // length 2
  }

  @struct class xcb_query_pointer_reply_t {
    val response_type: CUChar = extern
    val same_screen: CUChar = extern
    val sequence: CUShort = extern
    val length: CUInt = extern
    val root: xcb_window_t = extern
    val child: xcb_window_t = extern
    val root_x: CShort = extern
    val root_y: CShort = extern
    val win_x: CShort = extern
    val win_y: CShort = extern
    val mask: CUShort = extern
    val pad0: Array[CUChar] = extern // length 2
  }

  @struct class xcb_generic_error_t {
    val response_type: CUChar = extern
    val error_code: CUChar = extern
    val sequence: CUShort = extern
    val resource_id: CUInt = extern
    val minor_code: CUShort = extern
    val major_code: CUChar = extern
    val pad0: CUChar = extern
    val pad: Array[CUInt] = extern // length 5
    val full_sequence: CUInt = extern
  }


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
  def xcb_destroy_window(conn: Ptr[xcb_connection_t], window: xcb_window_t): xcb_void_cookie_t = extern

  def xcb_get_geometry(conn: Ptr[xcb_connection_t], drawable: xcb_drawable_t): xcb_get_geometry_cookie_t = extern
  def xcb_get_geometry_reply(conn: Ptr[xcb_connection_t], cookie: xcb_get_geometry_cookie_t, e: Ptr[Ptr[xcb_generic_error_t]]): Ptr[xcb_get_geometry_reply_t] = extern

  def xcb_warp_pointer(conn: Ptr[xcb_connection_t], src_window: xcb_window_t, dst_window: xcb_window_t, src_x: CShort, src_y: CShort, src_width: CUShort, src_height: CUShort, dst_x: CShort, dst_y: CShort): xcb_void_cookie_t = extern
  def xcb_grab_pointer(conn: Ptr[xcb_connection_t], owner_events: CUChar, grab_window: xcb_window_t, event_mask: CUShort, pointer_mode: CUChar, keyboard_mode: CUChar, confine_to: xcb_window_t, cursor: xcb_cursor_t, time: xcb_timestamp_t): xcb_grab_pointer_cookie_t = extern
  def xcb_ungrab_pointer(conn: Ptr[xcb_connection_t], time: xcb_timestamp_t): xcb_void_cookie_t = extern
  def xcb_query_pointer(conn: Ptr[xcb_connection_t], window: xcb_window_t): xcb_query_pointer_cookie_t = extern
  def xcb_query_pointer_reply(conn: Ptr[xcb_connection_t], cookie: xcb_query_pointer_cookie_t, e: Ptr[Ptr[xcb_generic_error_t]]): Ptr[xcb_query_pointer_reply_t] = extern

}

