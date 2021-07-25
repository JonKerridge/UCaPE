package examples.c22.universalClasses

import jcsp.lang.One2OneChannel


class InitObject implements Serializable {
	def id = 0
	One2OneChannel ChannelAddress = null
}
