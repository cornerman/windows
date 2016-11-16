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
      println("STATE:  " + state)
      println("EVENT:  " + event)
      val (newState, result) = dispatcher(state, event)
      println("RESULT: " + result)
      println("====")
      state = newState
      result
    } foreach println
  }

  def defaultDispatcher(state: State, event: Event): (State, EventResult) = Reactor.react(state, event) match {
    case Some(action) =>
      val (newState, todos) = Interpreter.interpret(state, action)
      (newState, EventResult(true,  todos))
    case None => (state, EventResult(false, Seq.empty))
  }
}
