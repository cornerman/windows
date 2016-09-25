package windows.wayland.wlc

import scala.scalanative.native._, Unsigned._

@link("wlc")
@extern object WLC {
  @struct class wlc_modifiers(
    val leds: CUInt,
    val mods: CUInt)

  @struct class wlc_point(
    val x: CInt,
    val y: CInt)

  @struct class wlc_size(
    val w: CUInt,
    val h: CUInt)

  @struct class wlc_geometry(
    val origin: wlc_point,
    val size: wlc_size)

  type wlc_handle = CULong // TODO: uintptr_t

  def wlc_init(): Boolean = extern
  def wlc_run(): Unit = extern
  def wlc_terminate(): Unit = extern;

  def wlc_exec(bin: CString, args: Array[CString]): Unit = extern

  /** Output was created. Return false if you want to destroy the output. (e.g. failed to allocate data related to view) */
  def wlc_set_output_created_cb(cb: FunctionPtr1[wlc_handle, Boolean]): Unit = extern

  /** Output was destroyed. */
  def wlc_set_output_destroyed_cb(cb: FunctionPtr1[wlc_handle, Unit]): Unit = extern

  /** Output got or lost focus. */
  def wlc_set_output_focus_cb(cb: FunctionPtr2[wlc_handle, Boolean, Unit]): Unit = extern

  /** Output resolution changed. */
  // void wlc_set_output_resolution_cb(void (*cb)(wlc_handle output, const struct wlc_size *from, const struct wlc_size *to));

  /** Output pre render hook. */
  // void wlc_set_output_render_pre_cb(void (*cb)(wlc_handle output));

  /** Output post render hook. */
  // void wlc_set_output_render_post_cb(void (*cb)(wlc_handle output));

  /** Output context is created. This generally happens on startup and when current tty changes */
  // void wlc_set_output_context_created_cb(void (*cb)(wlc_handle output));

  /** Output context was destroyed. */
  // void wlc_set_output_context_destroyed_cb(void (*cb)(wlc_handle output));

  /** View was created. Return false if you want to destroy the view. (e.g. failed to allocate data related to view) */
  def wlc_set_view_created_cb(cb: FunctionPtr1[wlc_handle, Boolean]): Unit = extern

  /** View was destroyed. */
  def wlc_set_view_destroyed_cb(cb: FunctionPtr1[wlc_handle, Unit]): Unit = extern

  /** View got or lost focus. */
  def wlc_set_view_focus_cb(cb: FunctionPtr2[wlc_handle, Boolean, Unit]): Unit = extern

  /** View was moved to output. */
  def wlc_set_view_move_to_output_cb(cb: FunctionPtr3[wlc_handle, wlc_handle, wlc_handle, Unit]): Unit = extern

  /** Request to set given geometry for view. Apply using wlc_view_set_geometry to agree. */
  // void wlc_set_view_request_geometry_cb(void (*cb)(wlc_handle view, const struct wlc_geometry*));

  /** Request to disable or enable the given state for view. Apply using wlc_view_set_state to agree. */
  // void wlc_set_view_request_state_cb(void (*cb)(wlc_handle view, enum wlc_view_state_bit, bool toggle));

  /** Request to move itself. Start a interactive move to agree. */
  // void wlc_set_view_request_move_cb(void (*cb)(wlc_handle view, const struct wlc_point*));

  /** Request to resize itself with the given edges. Start a interactive resize to agree. */
  // void wlc_set_view_request_resize_cb(void (*cb)(wlc_handle view, uint32_t edges, const struct wlc_point*));

  /** View pre render hook. */
  // void wlc_set_view_render_pre_cb(void (*cb)(wlc_handle view));

  /** View post render hook. */
  // void wlc_set_view_render_post_cb(void (*cb)(wlc_handle view));

  /** View properties (title, class, app_id) was updated */
  def wlc_set_view_properties_updated_cb(cb: FunctionPtr2[wlc_handle, CUInt, Unit]): Unit = extern

  /** Key event was triggered, view handle will be zero if there was no focus. Return true to prevent sending the event to clients. */
  def wlc_set_keyboard_key_cb(cb: FunctionPtr5[wlc_handle, CUInt, Ptr[wlc_modifiers], CUInt, wlc_key_state, Boolean]): Unit = extern

  /** Button event was triggered, view handle will be zero if there was no focus. Return true to prevent sending the event to clients. */
  def wlc_set_pointer_button_cb(cb: FunctionPtr5[wlc_handle, CUInt, Ptr[wlc_modifiers], CUInt, wlc_button_state, Boolean]): Unit = extern

  /** Scroll event was triggered, view handle will be zero if there was no focus. Return true to prevent sending the event to clients. */
  // void wlc_set_pointer_scroll_cb(bool (*cb)(wlc_handle view, uint32_t time, const struct wlc_modifiers*, uint8_t axis_bits, double amount[2]));

  /** Motion event was triggered, view handle will be zero if there was no focus. Apply with wlc_pointer_set_position to agree. Return true to prevent sending the event to clients. */
  def wlc_set_pointer_motion_cb(cb: FunctionPtr3[wlc_handle, CUInt, Ptr[wlc_point], Boolean]): Unit = extern

  /** Touch event was triggered, view handle will be zero if there was no focus. Return true to prevent sending the event to clients. */
  // void wlc_set_touch_cb(bool (*cb)(wlc_handle view, uint32_t time, const struct wlc_modifiers*, enum wlc_touch_type, int32_t slot, const struct wlc_point*));

  /** Compositor is ready to accept clients. */
  def wlc_set_compositor_ready_cb(cb: FunctionPtr0[Unit]): Unit = extern

  /** Compositor is about to terminate */
  def wlc_set_compositor_terminate_cb(cb: FunctionPtr0[Unit]): Unit = extern

  /** Input device was created. Return value does nothing. (Experimental) */
  // void wlc_set_input_created_cb(bool (*cb)(struct libinput_device *device));

  /** Input device was destroyed. (Experimental) */
  // void wlc_set_input_destroyed_cb(void (*cb)(struct libinput_device *device));

  /** Set log handler. Can be set before wlc_init. */
  def wlc_log_set_handler(cb: FunctionPtr2[wlc_log_type, CString, Unit]): Unit = extern

  /** Set state bit. Toggle indicates whether it is set or not. */
  def wlc_view_set_state(view: wlc_handle, state: wlc_view_state_bit, toggle: Boolean): Unit = extern

  /** Set visibility bitmask. */
  def wlc_view_set_mask(view: wlc_handle, mask: CUInt): Unit = extern

  /** Bring to front of everything. */
  def wlc_view_bring_to_front(view: wlc_handle): Unit = extern

  /** Focus view. Pass zero for no focus. */
  def wlc_view_focus(view: wlc_handle): Unit = extern

  /** Close view. */
  def wlc_view_close(view: wlc_handle): Unit = extern

  /** Get current output. */
  def wlc_view_get_output(view: wlc_handle): wlc_handle = extern

  /** Get current visibility bitmask. */
  def wlc_output_get_mask(output: wlc_handle): CUInt = extern

  /** Get current pointer position. */
  def wlc_pointer_get_position(out_position: Ptr[wlc_point]): Unit = extern

  /** Set current pointer position. */
  def wlc_pointer_set_position(position: Ptr[wlc_point]): Unit = extern

  /** Get current geometry. (what client sees) */
  def wlc_view_get_geometry(view: wlc_handle): Ptr[wlc_geometry] = extern

  /** Get visible geometry. (what wlc displays) */
  def wlc_view_get_visible_geometry(view: wlc_handle, out_geometry: Ptr[wlc_geometry]): Unit = extern

  /** Set geometry. Set edges if the geometry change is caused by interactive resize. */
  def wlc_view_set_geometry(view: wlc_handle, edges: wlc_resize_edge, geometry: Ptr[wlc_geometry]): Unit = extern
}
