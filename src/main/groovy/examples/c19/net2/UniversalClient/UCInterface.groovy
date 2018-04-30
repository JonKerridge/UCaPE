package examples.c19.net2.UniversalClient

import java.awt.*
import jcsp.awt.*
import groovyJCSP.PAR
import jcsp.lang.*



class UCInterface implements CSProcess {
	
	def ChannelOutput sendNodeIdentity
	
	void run (){
		def root = new ActiveClosingFrame ("Universal Client")
		def main = root.getActiveFrame()
		def requestlabel = new Label("Client IP Node Identity ?")
		def enterIPfield = new ActiveTextEnterField (null, sendNodeIdentity)
		def enterIP = enterIPfield.getActiveTextField ()
		def container = new Container()
		container.setLayout(new GridLayout(2,1))
		container.add(requestlabel)
		container.add(enterIP)
		main.setLayout(new BorderLayout())
		main.setSize(480, 640)
		main.add(container)
		main.pack()
		main.setVisible(true)
		new PAR([root, enterIPfield]).run()
	}
}
