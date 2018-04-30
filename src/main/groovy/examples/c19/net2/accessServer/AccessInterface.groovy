package examples.c19.net2.accessServer

import jcsp.lang.*
import java.awt.*
import jcsp.awt.*
import groovyJCSP.PAR



class AccessInterface implements CSProcess {
	
	def ChannelOutput buttonEvents
	
	void run (){
		def root = new ActiveClosingFrame ("Access Server")
		def main = root.getActiveFrame()
		def groupService = new ActiveButton ( null, buttonEvents, "Group Location Service")
		def serviceA = new ActiveButton ( null, buttonEvents, "Service - A")
		def serviceB = new ActiveButton ( null, buttonEvents, "Service - B")
		def serviceC = new ActiveButton ( null, buttonEvents, "Service - C")
		def container = new Container()
		container.setLayout(new GridLayout(4,1))
		container.add(groupService)
		container.add(serviceA)
		container.add(serviceB)
		container.add(serviceC)
		main.setLayout(new BorderLayout())
		main.setSize(480, 640)
		main.add(container)
		main.pack()
		main.setVisible(true)
		new PAR([root, groupService, serviceA, serviceB, serviceC]).run()

	}
}
