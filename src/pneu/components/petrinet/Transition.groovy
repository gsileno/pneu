package pneu.components.petrinet

abstract class Transition extends Node {

    // transition by default of normal type
    TransitionType type = TransitionType.NORMAL

    Boolean isEmitter() {
        return (type == TransitionType.EMITTER)
    }

    Boolean isCollector() {
        return (type == TransitionType.COLLECTOR)
    }

    // useful functions
    abstract Transition minimalClone()
    abstract Boolean compare(Transition target)
    abstract Boolean subsumes(Transition other)

    // operational semantics
    abstract Boolean isEnabledIncludingEmission()

    abstract Boolean isEnabled()

    // it returns the content produced as
    // a token with the label of the transition
    // (no anonymous variables generated for the places)
    abstract TransitionEvent fire()
    abstract void consumeInputTokens()
    abstract TransitionEvent produceOutputTokens()

    // to direct the firing for a specific event of the transition (introduced because of LPPN)
    abstract TransitionEvent fire(TransitionEvent event)
    abstract void consumeInputTokens(TransitionEvent event)
    abstract TransitionEvent produceOutputTokens(TransitionEvent event)

    // return all the possible fireable events (introduced because of LPPN)
    abstract List<TransitionEvent> fireableEvents()
}
