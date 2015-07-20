# pneu

Petri Net pnEUmatics: *groovy libraries/scripts to tinker with Petri Nets*

always valid guidelines:

* keep the source code simple, readable with few comments
* hopefully no documentation required
* make tests for each component

## Components

* parser of Yasper PNML (Petri Net Markup Language, XML based)
* converter to tikz (LaTeX!)
* converter to dot (graphviz, etc.)
* simulator 
* analyzer 

## Usage

**PNML parsing:**
```
#!groovy
import org.leibnizcenter.pneu.components.petrinet.Net
import org.leibnizcenter.pneu.parsers.pneu
Net net = pneu.parseFile("examples/basic/0emptyplace.pnml")
```

**tikz (LaTeX) conversion:**
```
#!groovy
import org.leibnizcenter.pneu.builders.PN2LaTeX
println(PN2LaTeX.convertabsolute(net)
```

**dot conversion:**
```
#!groovy
import org.leibnizcenter.pneu.builders.PN2dot
println(PN2dot.simpleConversion(net))
```

**monolithic simulation**
```
#!groovy
import org.leibnizcenter.pneu.animation.monolithic.PNRunner

PNRunner runner = new PNRunner() // brute-force based animation
runner.load(net) // load the petri net structure
runner.run(100)  // run at most 100 steps (less if there are no more enabled transitions)
```

## Contributing

1. Fork it
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create new Pull Request