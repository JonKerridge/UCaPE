package exercises.c2

import jcsp.lang.*
import groovy_jcsp.*

One2OneChannel connect1 = Channel.one2one()
One2OneChannel connect2 = Channel.one2one()

def processList = [ new GenerateSetsOfThree ( outChannel: connect1.out() ),
                    new ListToStream ( inChannel: connect1.in(), 
                    		           outChannel: connect2.out() ),
                    new CreateSetsOfEight ( inChannel: connect2.in() )
                  ]
new PAR (processList).run()