package windows

import scala.scalanative.native._, stdio._
import windows.msg._

object WM {
  type Runner = (Event => EventResult) => Option[String]
  type Dispatcher = (State, Event) => (State, EventResult)

  def run(config: Config, runner: Runner, dispatcher: Dispatcher = defaultDispatcher) {
    setvbuf(stdout, null, _IOLBF, 0) // line buffered

    var state = State.initial(config)

    runner { event =>
      println(state); println(event)
      val (newState, result) = dispatcher(state, event)
      println(newState); println(result)
      state = newState
      result
    } foreach println
  }

  def defaultDispatcher(state: State, event: Event): (State, EventResult) = Reactor.react(state, event) match {
    case Some(action) =>
      val newState = Interpreter.interpret(state, action)
      val actions = Layouter.layout(newState, action)
      val connActions = actions collect { case a: ConnectionAction => a }
      (newState, EventResult(true,  connActions))
    case None => (state, EventResult(false, Seq.empty))
  }
}
