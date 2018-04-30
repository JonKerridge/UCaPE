package examples.c20.net2
   
import jcsp.lang.*
import groovyJCSP.*
import groovyJCSP.plugAndPlay.* 




class AgentElement implements CSProcess {
  
  def ChannelInput fromRing
  def ChannelOutput toRing
  def int element
  def int nodes
  def int iterations
  
  def void run() {
    def S2RE = Channel.one2one()
    def RE2Q = Channel.one2one()
    def SM2RE = Channel.one2one()
    def Q2P = Channel.one2one()
    def Q2SM = Channel.one2one()
    def P2Q = Channel.one2one()
    def P2R = Channel.one2one()
    def R2GEC = Channel.one2one()
    def R2GECClear = Channel.one2one()
    def GEC2R = Channel.one2one()
    
    def nodeList = [ new Sender ( toElement: S2RE.out(), 
                                   element: element, 
                                   nodes: nodes, 
                                   iterations: iterations),
                     new Receiver ( fromElement: P2R.in(), 
                                    fromConsole: GEC2R.in(),
                                    clear: R2GECClear.out(),
                                    outChannel: R2GEC.out() ),
                     new RingAgentElement ( fromSender: S2RE.in(),
                                            fromStateManager: SM2RE.in(),
                                            toQueue: RE2Q.out(),
                                            fromRing: fromRing, 
                                            toRing: toRing, 
                                            element: element ), 
                     new Queue ( fromElement: RE2Q.in(),
                                 toStateManager: Q2SM.out(),
                                 fromPrompter:P2Q.in() ,
                                 toPrompter: Q2P.out(),
                                 slots: (nodes * 2) ),                           
                     new Prompter ( toQueue: P2Q.out(),
                                     fromQueue: Q2P.in(),
                                     toReceiver: P2R.out()), 
                     new StateManager ( fromQueue: Q2SM.in(),
                                         toElement: SM2RE.out(),
                                         queueSlots: (nodes * 2)  ),
                     new GConsole ( toConsole: R2GEC.in(),
                                    fromConsole: GEC2R.out(),
                                    clearInputArea: R2GECClear.in(),
                                    frameLabel: "Element: " + element)
                   ]
    new PAR ( nodeList ).run()
  }
}
    
