package windows

import windows.msg._
import windows.system.SystemAdapter
import scalanative.native.stdio._

object WM {
  def run(config: Config,
          runner: (Event => Unit) => Unit,
          reactor: (Config, Mode, Event) => Option[Action],
          dispatcher: (State, Action) => State) {

    setvbuf(stdout, null, _IOLBF, 0) // line buffered

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

  def run(runner: (Event => Unit) => Unit, act: ConnectionAction => Option[String]) {
    import ActionDispatch._
    val dispatcher = chain(onConnection(act), onSystem(SystemAdapter.act), onInterpreter(Tiling.interpret)) _
    run(MyConfig, runner, Reactor.react, dispatcher)
  }
}
