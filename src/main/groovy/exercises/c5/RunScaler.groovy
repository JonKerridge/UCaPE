package exercises.c5
 
import jcsp.lang.*
import groovyJCSP.*
import groovyJCSP.plugAndPlay.* 
import examples.c05.Controller
import examples.c05.ScaledData
  
def data = Channel.createOne2One()
def timedData = Channel.createOne2One()
def scaledData = Channel.createOne2One()
def oldScale = Channel.createOne2One()
def newScale = Channel.createOne2One()
def pause = Channel.createOne2One()

def network = [ new GNumbers ( outChannel: data.out() ),
                new GFixedDelay ( delay: 1000, 
                		          inChannel: data.in(), 
                		          outChannel: timedData.out() ),
                new Scale ( inChannel: timedData.in(),
                            outChannel: scaledData.out(),
                            factor: oldScale.out(),
                            suspend: pause.in(),
                            injector: newScale.in(),
                            scaling: 2 ),
                new Controller ( testInterval: 7000,
                		         computeInterval: 700,
                                 factor: oldScale.in(),
                                 suspend: pause.out(),
                                 injector: newScale.out() ),
                new GPrint ( inChannel: scaledData.in(),
                		     heading: "Original      Scaled",
                		     delay: 0)
              ]
new PAR ( network ).run()                                                            
