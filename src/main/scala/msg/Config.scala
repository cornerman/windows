package windows.msg

object Modifier {
  type ModType = Int
  val Shift = 1
  val Lock = 2
  val Ctrl = 3
  val Mod1 = 4
  val Mod2 = 5
  val Mod3 = 6
  val Mod4 = 7
  val Mod5 = 8
}

trait Config {
  val mod = Modifier.Mod1 //alt
  val moveButton = 1 //left
  val resizeButton = 3 //right
  val exitKey = 26 //e
  val closeKey = 24 //q
  val execKey = 40 //d
  val execCmd = "urxvt"
}
