package windows.x

import scala.scalanative.native._
import xcb._, XCB._

object Event {
  type EventPtr = Ptr[xcb_generic_event_t]

  def as[T](e: EventPtr, response: Int): Option[Ptr[T]] = {
    if (response == ((!e).response_type & ~0x80)) Some(e.cast[Ptr[T]]) else None
  }
}
import Event._

object ButtonPressEvent {
  def unapply(e: EventPtr) = as[xcb_button_press_event_t](e, XCB_BUTTON_PRESS)
}

object ButtonReleaseEvent {
  def unapply(e: EventPtr) = as[xcb_button_release_event_t](e, XCB_BUTTON_RELEASE)
}

object KeyPressEvent {
  def unapply(e: EventPtr) = as[xcb_key_press_event_t](e, XCB_KEY_PRESS)
}

object KeyReleaseEvent {
  def unapply(e: EventPtr) = as[xcb_key_release_event_t](e, XCB_KEY_RELEASE)
}

object MotionNotifyEvent {
  def unapply(e: EventPtr) = as[xcb_motion_notify_event_t](e, XCB_MOTION_NOTIFY)
}

object MapRequestEvent {
  def unapply(e: EventPtr) = as[xcb_map_request_event_t](e, XCB_MAP_REQUEST)
}

object MapNotifyEvent {
  def unapply(e: EventPtr) = as[xcb_map_notify_event_t](e, XCB_MAP_NOTIFY)
}

object UnmapNotifyEvent {
  def unapply(e: EventPtr) = as[xcb_unmap_notify_event_t](e, XCB_UNMAP_NOTIFY)
}

// object DestroyNotifyEvent {
//   def unapply(e: EventPtr) = as[xcb_destroy_notify_event_t](e, XCB_DESTROY_NOTIFY)
// }

// object ExposeEvent {
//   def unapply(e: Event) = as[xcb_expose_event_t](e, XCB_EXPOSE)
// }

// object FocusInEvent {
//   def unapply(e: Event) = as[xcb_focus_in_event_t](e, XCB_FOCUS_IN)
// }
