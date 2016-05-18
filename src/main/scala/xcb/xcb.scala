package windows.xcb

import scalanative.native._

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

  @struct class xcb_setup_t {
    val has_error: CInt = extern
    val fd: CInt = extern
    val status: CUnsignedChar = extern
    val pad0: CUnsignedChar = extern
    val protocol_major_version: CUnsignedChar = extern
    val protocol_minor_version: CUnsignedChar = extern
    val length: CUnsignedChar = extern
    val release_number: CUnsignedInt = extern
    val resource_id_base: CUnsignedInt = extern
    val resource_id_mask: CUnsignedInt = extern
    val motion_buffer_size: CUnsignedInt = extern
    val vendor_len: CUnsignedChar = extern
    val maximum_request_length: CUnsignedChar = extern
    val roots_len: CUnsignedChar = extern
    val pixmap_formats_len: CUnsignedChar = extern
    val image_byte_order: CUnsignedChar = extern
    val bitmap_format_bit_order: CUnsignedChar = extern
    val bitmap_format_scanline_unit: CUnsignedChar = extern
    val bitmap_format_scanline_pad: CUnsignedChar = extern
    val min_keycode: xcb_keycode_t = extern
    val max_keycode: xcb_keycode_t = extern
    val pad1: CUnsignedChar = extern
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
    val white_pixel: CUnsignedInt = extern
    val black_pixel: CUnsignedInt = extern
    val current_input_masks: CUnsignedInt = extern
    val width_in_pixels: CUnsignedShort = extern
    val height_in_pixels: CUnsignedShort = extern
    val width_in_millimeters: CUnsignedShort = extern
    val height_in_millimeters: CUnsignedShort = extern
    val min_installed_maps: CUnsignedShort = extern
    val max_installed_maps: CUnsignedShort = extern
    val root_visual: xcb_visualid_t = extern
    val backing_stores: CUnsignedChar = extern
    val save_unders: CUnsignedChar = extern
    val root_depth: CUnsignedChar = extern
    val allowed_depths_len: CUnsignedChar = extern
  }

  @struct class xcb_generic_event_t {
    val response_type: CChar = extern //TODO: CUnsignedChar...
    val pad0: CUnsignedChar = extern
    val sequence: CUnsignedShort = extern
    val pad: Array[CUnsignedInt] = extern // length 7
    val full_sequence: CUnsignedInt = extern
  }

  def xcb_connect(displayname: CString, screenp: Ptr[Int]): Ptr[xcb_connection_t] = extern
  def xcb_connection_has_error(c: Ptr[xcb_connection_t]): CInt = extern
  def xcb_get_setup(c: Ptr[xcb_connection_t]): Ptr[xcb_setup_t] = extern
  def xcb_setup_roots_iterator(s: Ptr[xcb_setup_t]): Ptr[xcb_screen_t] = extern
  def xcb_flush(c: Ptr[xcb_connection_t]): CInt = extern
  def xcb_disconnect(c: Ptr[xcb_connection_t]): Unit = extern

  def xcb_grab_key(c: Ptr[xcb_connection_t], owner_events: CUnsignedChar, grab_window: xcb_window_t, modifiers: CUnsignedShort, key: xcb_keycode_t, pointer_mode: CUnsignedChar, keyboard_mode: CUnsignedChar): Any = extern //TODO xcb_void_cookie_t + xcb_request_check with xcb_grab_key_checked
  def xcb_grab_button(c: Ptr[xcb_connection_t], owner_events: CUnsignedChar, grab_window: xcb_window_t, event_mask: CUnsignedShort, pointer_mode: CUnsignedChar, keyboard_mode: CUnsignedChar, confine_to: xcb_window_t, cursor: xcb_cursor_t, button: CUnsignedChar, modifiers: CUnsignedShort): Any = extern //TODO xcb_void_cookie_t + xcb_request_check with xcb_grab_button_checked

  def xcb_wait_for_event(c: Ptr[xcb_connection_t]): Ptr[xcb_generic_event_t] = extern
  def xcb_poll_for_event(c: Ptr[xcb_connection_t]): Ptr[xcb_generic_event_t] = extern
}

