package windows

import windows.msg._

object MyConfig extends Config

object WM {
  def run(config: Config,
          runner: (Event => Unit) => Option[String],
          reactor: (Config, Mode, Event) => Option[Action],
          dispatcher: (State, Action) => State): Option[String] = {

    var mode = Mode.initial
    var state = State.initial(config)

    dispatcher(state, Configure(config))
    runner { event =>
      reactor(state.config, mode, event) foreach { action =>
        state = dispatcher(state, action)
        if (state.errors.nonEmpty) {
          // val msg = state.errors.mkString(",")
          val msg = "has errors" //TODO: why does this throw?
          dispatcher(state, Exit(msg))
        }
      }

      mode = mode.update(event)
    }
  }

  def run(runner: (Event => Unit) => Option[String], act: ConnectionAction => Option[String]): Option[String] = {
    import ActionDispatch._
    val dispatcher = chain(onConnection(act), onInterpreter(Tiling.interpret)) _
    run(MyConfig, runner, Reactor.react _, dispatcher)
  }
}
