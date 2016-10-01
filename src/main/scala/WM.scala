package windows

import windows.msg._

object WM {
  def run(config: Config,
          runner: (Event => Boolean) => Option[String],
          reactor: (Config, Mode, Event) => Seq[Action],
          dispatcher: (State, Action) => State): Option[String] = {

    var mode = Mode.initial
    var state = State.initial(config)

    dispatcher(state, Configure(config))
    runner { event =>
      println(event)
      val actions = reactor(state.config, mode, event)
      actions foreach { action =>
        println(action)
        state = dispatcher(state, action)
        if (state.errors.nonEmpty) {
          val msg = state.errors.mkString(",")
          dispatcher(state, Exit(msg))
        }
      }

      mode = mode.update(event)
      actions.nonEmpty
    }
  }

  def run(config: Config, runner: (Event => Boolean) => Option[String], act: ConnectionAction => Option[String]): Option[String] = {
    import ActionDispatch._
    val dispatcher = chain(onConnection(act), onInterpreter(Tiling.interpret)) _
    run(config, runner, Reactor.react _, dispatcher)
  }
}
