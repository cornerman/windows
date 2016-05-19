package windows

import scala.scalanative.native._
import xcb._, XCB._

@struct class Event(val response: Int, val event: Ptr[xcb_generic_event_t])

object Event {
  def as[T](e: Event, response: Int): Option[Ptr[T]] = if (response == e.response) Some(e.event.cast[Ptr[T]]) else None
  def apply(response: Int, event: Ptr[xcb_generic_event_t]) = new Event(response, event)
}

object ButtonPressEvent {
  def unapply(e: Event) = Event.as[xcb_button_press_event_t](e, XCB_BUTTON_PRESS)
}

object ButtonReleaseEvent {
  def unapply(e: Event) = Event.as[xcb_button_release_event_t](e, XCB_BUTTON_RELEASE)
}

object KeyPressEvent {
  def unapply(e: Event) = Event.as[xcb_key_press_event_t](e, XCB_KEY_PRESS)
}

object KeyReleaseEvent {
  def unapply(e: Event) = Event.as[xcb_key_release_event_t](e, XCB_KEY_RELEASE)
}

object MotionNotifyEvent {
  def unapply(e: Event) = Event.as[xcb_motion_notify_event_t](e, XCB_MOTION_NOTIFY)
}
