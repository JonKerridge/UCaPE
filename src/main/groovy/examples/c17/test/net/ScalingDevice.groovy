package examples.c17.test.net

import jcsp.lang.*
import groovyJCSP.*
import examples.c05.*


class ScalingDevice implements CSProcess {

  def ChannelInput inChannel
  def ChannelOutput outChannel
  
  void run() {
    def oldScale = Channel.createOne2One()
    def newScale = Channel.createOne2One()
    def pause = Channel.createOne2One()

    def scaler = new Scale ( inChannel: inChannel,
                              outChannel: outChannel,
                              factor: oldScale.out(),
                              suspend: pause.in(),
                              injector: newScale.in(),
                              multiplier: 2,
                              scaling: 2 )

    def control =  new Controller ( testInterval: 7000,
                                     computeInterval: 700,
                                     addition: 1,
                                     factor: oldScale.in(),
                                     suspend: pause.out(),
                                     injector: newScale.out() )
    
    def testList = [ scaler, control]
    
    new PAR(testList).run()                 
  }

}