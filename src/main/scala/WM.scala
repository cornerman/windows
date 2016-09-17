package windows

import windows.msg._
import windows.system.SystemAdapter

object WM {
  def run(config: Config,
          runner: (Event => Unit) => Unit,
          reactor: (Config, Mode, Event) => Option[Action],
          dispatcher: Action => Unit,
          interpreter: (State, Action) => State) {

    var mode = Mode.initial
    var state = State.initial(config)

    dispatcher(Configure(config))
    runner { event =>
      val actionOpt = reactor(state.config, mode, event)
      val newState = actionOpt map { action =>
        //TODO error handling from dispatch
        dispatcher(action)
        interpreter(state, action)
      }

      mode = mode.update(event)
      newState foreach { state = _ }

      import scalanative.native.stdio._
      fflush(stdout)
    }
  }

  def run(runner: (Event => Unit) => Unit, act: ConnectionAction => Unit) {
    run(MyConfig, runner, Reactor.react, ActionDispatch(SystemAdapter.act, act), Tiling.interpret)
  }
}
