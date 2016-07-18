package pneu.animation.monolithic.execution

import pneu.animation.monolithic.analysis.State
import pneu.components.petrinet.Net
import pneu.components.petrinet.Place
import pneu.components.petrinet.Transition
import pneu.components.petrinet.Node
import pneu.components.petrinet.TransitionEvent

/* Implementation notes

We consider four types of executions:
* Brute Force (BF).
- All transitions are tested for firing. No improvement of the search of the enabled transitions.

Transition-driven approaches
* Enabled Transitions (ET).
  An alternative characterization of the enabling of transitions is provided (different from marking)
  It takes into account the local topology of the transitions.
  Two approaches:
  - for binary lpetri nets: a counter of non marked input places of a transition is used (Silva and Velilla 1982).
    If the counter goes to zero, the transition is enabled.
  - for bounded weighted PN: linear enabling functions (Briz and Colom 1994)

Plan-driven approaches:
Each transition is represented by only one of its input places, the Representing Place.
All the remaining input places are called synchronization places.
Only transitions whose representing place is marked are considered as candidates for firing.
There are two versions:
* Static Representing Places (SRP)
* Dynamic Representing Places (DRP)
- if a representing place is marked but the transition is not enabled due to one or
  several of the synchronization places being disabled,
  then any of the remaining unmarked synchronization places is chosen as the new representing place.

Further literature:

Moreno, R. P., & Salcedo, J. L. V. (2007).
Performance evaluation of lpetri nets execution algorithms.
Conference Proceedings - IEEE International Conference on Systems, Man and Cybernetics, 1400–1407.

Piedrafita, R., & Villarroel, J. L. (2011).
Performance evaluation of lpetri nets centralized implementation.
The execution time controller. Discrete Event Dynamic Systems: Theory and Applications,
21(2), 139–169.

*/

abstract class Execution {

    Net net

    // to keep trace of tokens emitted/collected
    Integer nTokenEmitted = 0
    Integer nTokenCollected = 0

    // perform one execution step
    abstract Boolean step()

    // to fire a transition
    abstract TransitionEvent fire(Transition t)

    // when you already know what to execute (resuming after analysis)
    // the functions returns the content fired as a token
    // (pay attention: it does not include the possible anonymous parameters generated for the places)
    abstract TransitionEvent fire(TransitionEvent event)

    // TODO: check the state after the run! we should put again at state 0
    // load the net
    void load(Net source) {
        net = source.reifyNetFunction()
    }

    // load a state
    void loadState(State state) {
        for (p in places) {
            p.marking = state.placeIdTokensMap[p.id].collect()
        }
    }

    List<Transition> getTransitions() {
        return net.transitionList
    }

    List<Place> getPlaces() {
        return net.placeList
    }

    List<Node> getInputs() {
        return net.inputs
    }

    // return emitters as the inputs of the net
    // TODO: check also the explicit EMITTERS
    List<Transition> getEmitterInputs() {
        List<Transition> emitterInputs = []
        for (input in net.inputs) {
            if (input.isTransitionLike()) {
                emitterInputs << (Transition) input
            }
        }
        emitterInputs
    }

    List<TransitionEvent> firedEmitterEventList = []
}