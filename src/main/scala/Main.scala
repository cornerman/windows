package windows

import scalanative.native._, Unsigned._
import native.stdio._
import xcb._, XCB._

object Main {
  def main(args: Array[String]): Unit = {
    val conn = xcb_connect(null, null);
    if (xcb_connection_has_error(conn) > 0) {
      fprintf(stderr, c"Cannot connect to x")
      return
    }

    val setup = xcb_get_setup(conn)
    val screen = xcb_setup_roots_iterator(setup)
    val root = (!screen).root

    xcb_grab_key(conn, 0.toUByte, root, XCB_MOD_MASK_ANY.toUShort, 67.toUByte, XCB_GRAB_MODE_ASYNC, XCB_GRAB_MODE_ASYNC)
    xcb_grab_key(conn, 0.toUByte, root, XCB_MOD_MASK_1.toUShort, 24.toUByte, XCB_GRAB_MODE_ASYNC, XCB_GRAB_MODE_ASYNC)

    xcb_grab_button(conn, 0.toUByte, root, (XCB_EVENT_MASK_BUTTON_PRESS | XCB_EVENT_MASK_BUTTON_RELEASE).toUShort, XCB_GRAB_MODE_ASYNC, XCB_GRAB_MODE_ASYNC, root, XCB_NONE, 1.toUByte, XCB_MOD_MASK_1.toUShort)

    xcb_grab_button(conn, 0.toUByte, root, (XCB_EVENT_MASK_BUTTON_PRESS | XCB_EVENT_MASK_BUTTON_RELEASE).toUShort, XCB_GRAB_MODE_ASYNC, XCB_GRAB_MODE_ASYNC, root, XCB_NONE, 3.toUByte, XCB_MOD_MASK_1.toUShort)

    xcb_flush(conn)

    while (true) {
      val ev = xcb_wait_for_event(conn)
      val response = (!ev).response_type & ~0x80
      printf(c"response %i\n", response)
      response match {
        case `XCB_BUTTON_PRESS` =>
          printf(c"button press")
          // val bpe = ev.cast[Ptr[xcb_button_press_event_t]]
          // val win = e->child
          // values[0] = Array(XCB_STACK_MODE_ABOVE)
          // xcb_configure_window(conn, win, XCB_CONFIG_WINDOW_STACK_MODE, values)
          // //xcb_get_geometry_reply_t *geom
          // val geom = xcb_get_geometry_reply(conn, xcb_get_geometry(conn, win), NULL)
          // if (1 == e->detail) {
          //     values[2] = 1
          //     xcb_warp_pointer(conn, XCB_NONE, win, 0, 0, 0, 0, 1, 1)
          // } else {
          //     values[2] = 3
          //     xcb_warp_pointer(conn, XCB_NONE, win, 0, 0, 0, 0, geom->width, geom->height)
          // }
          // xcb_grab_pointer(conn, 0, root, XCB_EVENT_MASK_BUTTON_RELEASE | XCB_EVENT_MASK_BUTTON_MOTION | XCB_EVENT_MASK_POINTER_MOTION_HINT, XCB_GRAB_MODE_ASYNC, XCB_GRAB_MODE_ASYNC, root, XCB_NONE, XCB_CURRENT_TIME)
          xcb_flush(conn)
        case `XCB_BUTTON_RELEASE` => printf(c"button release")
        case `XCB_KEY_PRESS` => printf(c"key press")
        case `XCB_KEY_RELEASE` => printf(c"key release")
        case `XCB_MOTION_NOTIFY` => printf(c"motion notify")
      }

      fflush(stdout)
    }

    // xcb_disconnect(conn)
  }
}


// #include <xcb/xcb.h>

// int main (int argc, char **argv)
// {

//     uint32_t values[3];

//     xcb_connection_t *conn;
//     xcb_screen_t *screen;
//     xcb_drawable_t win;
//     xcb_drawable_t root;

//     xcb_generic_event_t *ev;
//     xcb_get_geometry_reply_t *geom;

//     conn = xcb_connect(NULL, NULL);
//     if (xcb_connection_has_error(conn)) return 1;

//     screen = xcb_setup_roots_iterator(xcb_get_setup(conn)).data;
//     root = screen->root;

//     xcb_grab_key(conn, 1, root, XCB_MOD_MASK_2, XCB_NO_SYMBOL,
//                  XCB_GRAB_MODE_ASYNC, XCB_GRAB_MODE_ASYNC);

//     xcb_grab_button(conn, 0, root, XCB_EVENT_MASK_BUTTON_PRESS | 
//                 XCB_EVENT_MASK_BUTTON_RELEASE, XCB_GRAB_MODE_ASYNC, 
//                 XCB_GRAB_MODE_ASYNC, root, XCB_NONE, 1, XCB_MOD_MASK_1);

//     xcb_grab_button(conn, 0, root, XCB_EVENT_MASK_BUTTON_PRESS | 
//                 XCB_EVENT_MASK_BUTTON_RELEASE, XCB_GRAB_MODE_ASYNC, 
//                 XCB_GRAB_MODE_ASYNC, root, XCB_NONE, 3, XCB_MOD_MASK_1);
//     xcb_flush(conn);

//     for (;;)
//     {
//         ev = xcb_wait_for_event(conn);
//         switch (ev->response_type & ~0x80) {
        
//         case XCB_BUTTON_PRESS:
//         {
//             xcb_button_press_event_t *e;
//             e = ( xcb_button_press_event_t *) ev;
//             win = e->child; 
//             values[0] = XCB_STACK_MODE_ABOVE;
//             xcb_configure_window(conn, win, XCB_CONFIG_WINDOW_STACK_MODE, values);
//             geom = xcb_get_geometry_reply(conn, xcb_get_geometry(conn, win), NULL);
//             if (1 == e->detail) {
//                 values[2] = 1; 
//                 xcb_warp_pointer(conn, XCB_NONE, win, 0, 0, 0, 0, 1, 1);
//             } else {
//                 values[2] = 3; 
//                 xcb_warp_pointer(conn, XCB_NONE, win, 0, 0, 0, 0, geom->width, geom->height);
//             }
//             xcb_grab_pointer(conn, 0, root, XCB_EVENT_MASK_BUTTON_RELEASE
//                     | XCB_EVENT_MASK_BUTTON_MOTION | XCB_EVENT_MASK_POINTER_MOTION_HINT, 
//                     XCB_GRAB_MODE_ASYNC, XCB_GRAB_MODE_ASYNC, root, XCB_NONE, XCB_CURRENT_TIME);
//             xcb_flush(conn);
//         }
//         break;

//         case XCB_MOTION_NOTIFY:
//         {
//             xcb_query_pointer_reply_t *pointer;
//             pointer = xcb_query_pointer_reply(conn, xcb_query_pointer(conn, root), 0);
//             if (values[2] == 1) {/* move */
//                 geom = xcb_get_geometry_reply(conn, xcb_get_geometry(conn, win), NULL);
//                 values[0] = (pointer->root_x + geom->width > screen->width_in_pixels)?
//                     (screen->width_in_pixels - geom->width):pointer->root_x;
//                 values[1] = (pointer->root_y + geom->height > screen->height_in_pixels)?
//                     (screen->height_in_pixels - geom->height):pointer->root_y;
//                 xcb_configure_window(conn, win, XCB_CONFIG_WINDOW_X | XCB_CONFIG_WINDOW_Y, values);
//                 xcb_flush(conn);
//             } else if (values[2] == 3) { /* resize */
//                 geom = xcb_get_geometry_reply(conn, xcb_get_geometry(conn, win), NULL);
//                 values[0] = pointer->root_x - geom->x;
//                 values[1] = pointer->root_y - geom->y;
//                 xcb_configure_window(conn, win, XCB_CONFIG_WINDOW_WIDTH | XCB_CONFIG_WINDOW_HEIGHT, values);
//                 xcb_flush(conn);
//             }
//         }
//         break;

//         case XCB_BUTTON_RELEASE:
//             xcb_ungrab_pointer(conn, XCB_CURRENT_TIME);
//             xcb_flush(conn); 
//         break;
//         }
//     }

// return 0;
// }
