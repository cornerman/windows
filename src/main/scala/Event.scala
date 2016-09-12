package windows

import scala.scalanative.native._
import xcb._, XCB._

object Event {
  def as[T](e: Ptr[xcb_generic_event_t], response: UByte): Option[Ptr[T]] = (!e).response_type match {
    case `response` /*& ~0x80*/ => Some(e.cast[Ptr[T]])
    case _                  => None
  }
}

object ButtonPressEvent {
  def unapply(e: Ptr[xcb_generic_event_t]) = Event.as[xcb_button_press_event_t](e, XCB_BUTTON_PRESS)
}

object ButtonReleaseEvent {
  def unapply(e: Ptr[xcb_generic_event_t]) = Event.as[xcb_button_release_event_t](e, XCB_BUTTON_RELEASE)
}

object KeyPressEvent {
  def unapply(e: Ptr[xcb_generic_event_t]) = Event.as[xcb_key_press_event_t](e, XCB_KEY_PRESS)
}

object KeyReleaseEvent {
  def unapply(e: Ptr[xcb_generic_event_t]) = Event.as[xcb_key_release_event_t](e, XCB_KEY_RELEASE)
}

object MotionNotifyEvent {
  def unapply(e: Ptr[xcb_generic_event_t]) = Event.as[xcb_motion_notify_event_t](e, XCB_MOTION_NOTIFY)
}
