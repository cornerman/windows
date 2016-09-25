package windows.wayland

package object wlc {
  // enum wlc_modifier_bit {
  type wlc_modifier_bit = Int
  val WLC_BIT_MOD_SHIFT = 1 << 0
  val WLC_BIT_MOD_CAPS = 1 << 1
  val WLC_BIT_MOD_CTRL = 1 << 2
  val WLC_BIT_MOD_ALT = 1 << 3
  val WLC_BIT_MOD_MOD2 = 1 << 4
  val WLC_BIT_MOD_MOD3 = 1 << 5
  val WLC_BIT_MOD_LOGO = 1 << 6
  val WLC_BIT_MOD_MOD5 = 1 << 7

  // enum wlc_led_bit {
  type wlc_led_bit = Int
  val WLC_BIT_LED_NUM = 1 << 0
  val WLC_BIT_LED_CAPS = 1 << 1
  val WLC_BIT_LED_SCROLL = 1 << 2

  // enum wlc_key_state {
  type wlc_key_state = Int
  val WLC_KEY_STATE_RELEASED = 0
  val WLC_KEY_STATE_PRESSED = 1

  // enum wlc_button_state {
  type wlc_button_state = Int
  val WLC_BUTTON_STATE_RELEASED = 0
  val WLC_BUTTON_STATE_PRESSED = 1

  // enum wlc_view_state_bit {
  type wlc_view_state_bit = Int
  val WLC_BIT_MAXIMIZED = 1<<0
  val WLC_BIT_FULLSCREEN = 1<<1
  val WLC_BIT_RESIZING = 1<<2
  val WLC_BIT_MOVING = 1<<3
  val WLC_BIT_ACTIVATED = 1<<4

  //enum wlc_log_type {
  type wlc_log_type = Int
  val WLC_LOG_INFO = 0
  val WLC_LOG_WARN = 1
  val WLC_LOG_ERROR = 2
  val WLC_LOG_WAYLAND = 3

  //enum wlc_resize_edge {
  type wlc_resize_edge = Int
  val WLC_RESIZE_EDGE_NONE = 0
  val WLC_RESIZE_EDGE_TOP = 1
  val WLC_RESIZE_EDGE_BOTTOM = 2
  val WLC_RESIZE_EDGE_LEFT = 4
  val WLC_RESIZE_EDGE_TOP_LEFT = 5
  val WLC_RESIZE_EDGE_BOTTOM_LEFT = 6
  val WLC_RESIZE_EDGE_RIGHT = 8
  val WLC_RESIZE_EDGE_TOP_RIGHT = 9
  val WLC_RESIZE_EDGE_BOTTOM_RIGHT = 10
}
