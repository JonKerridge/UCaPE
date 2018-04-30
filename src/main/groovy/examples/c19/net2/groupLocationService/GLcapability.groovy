package examples.c19.net2.groupLocationService

import jcsp.lang.*
import jcsp.net2.*

class GLcapability implements CSProcess, Serializable {
	
	def ChannelInput nameChannel
	def ChannelInput locationChannel
	def ChannelOutput label1Config
	def ChannelOutput label2Config
	def NetChannelLocation requestLocation
	
	void run(){
		def responseChannel = NetChannel.net2one()
		def responseLocation = responseChannel.getLocation()
		def requestChannel = NetChannel.any2net(requestLocation)
		def groupName = nameChannel.read()
		def groupData = new GLdata( responseLocation: responseLocation,
																groupName: groupName)  
		requestChannel.write(groupData) 
		def replyData = responseChannel.read()
    
		if (replyData.location != null){
			label1Config.write("Meeting at")
			label2Config.write(replyData.location)
		}
		else {
			label1Config.write("Does not yet exist")
			label2Config.write("Type meeting location")
			def location = locationChannel.read()
			def newGroup = new GLdata( responseLocation: responseLocation,
																 groupName: groupName,
																 location: location)
			requestChannel.write(newGroup)
		}
	}// end run
}
