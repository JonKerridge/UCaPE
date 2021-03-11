package exercises.c2
    
import jcsp.lang.*
import groovy_jcsp.*
import examples.c02.Producer
     
One2OneChannel connect1 = Channel.createOne2One()
One2OneChannel connect2 = Channel.createOne2One()

def processList = [ new Producer ( outChannel: connect1.out() ),
                    //insert here an instance of multiplier with a multiplication factor of 4
                    new Consumer ( inChannel: connect2.in() )
                  ]
new PAR (processList).run()