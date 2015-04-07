package org.leibnizcenter.pneu.animation.monolithic.execution

import org.leibnizcenter.pneu.components.petrinet.Net

/* Implementation notes

We consider four types of executions:
* Brute Force (BF).
- All transitions are tested for firing. No improvement of the search of the enabled transitions.

Transition-driven approaches
* Enabled Transitions (ET).
  An alternative characterization of the enabling of transitions is provided (different from marking)
  It takes into account the local topology of the transitions.
  Two approaches:
  - for binary petri nets: a counter of non marked input places of a transition is used (Silva and Velilla 1982).
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
Performance evaluation of petri nets execution algorithms.
Conference Proceedings - IEEE International Conference on Systems, Man and Cybernetics, 1400–1407.

Piedrafita, R., & Villarroel, J. L. (2011).
Performance evaluation of petri nets centralized implementation.
The execution time controller. Discrete Event Dynamic Systems: Theory and Applications,
21(2), 139–169.

*/

enum ExecutionMode { BruteForce, EnabledTransition, StaticRepresentingPlaces, DynamicRepresentingPlaces }

interface Execution {

    // load the net
    void load(Net net)

    // returns false if there are no more enabled transitions
    Boolean step()
}