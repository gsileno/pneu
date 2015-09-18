*TODO*
[monolithic] consider the firing of multiple transitions with the same label
[monolithic analysis] complete it and refactor everything
[monolithic analysis] in particular, state should be defined saving the local state of all places and transitions, defined by the different implementations
[monolithic] refactor execution as interface
[graphics] consider biflows connections
[builders PN2LaTeX] avoid to write labels on top of the arrows
[builders PN2dot] implement an absolute conversion

*ERRORS*
[monolithic animation] enabled-transition executions do not work
[monolithic animation] place-based executions do not work
[monolithic animation] after changing the semantics to one transition per step I have to reformulate many things
[actorbased animation] does not work
[builders PN2LaTeX] does not consider hierarchical nets
[builders PN2ASP] does not consider hierarchical nets
[builders PN2PNML] does not consider hierarchical nets
[builders PN2LaTeX] convertrelative2 does not work perfectly, with actionscheme.pnml it lose one place and attach it to the transition
[components] error in net comparison with link transitions!!!

*INFO*
[builders PN2LaTeX] convertrelative1 is not the best strategy, should be discarded

